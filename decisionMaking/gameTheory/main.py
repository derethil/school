from abc import ABC, abstractmethod
from argparse import ArgumentParser
from enum import Enum
from random import shuffle
from types import NoneType
from typing import Optional
import logging

import numpy as np
import matplotlib.pyplot as plt


logger = logging.getLogger("game_theory")

# ============
# Config
# ============

config = {"agent_overlap": False}

# ============
# Agents
# ============


class AgentOutcome(Enum):
    DIE = 0
    SURVIVE = 1
    REPRODUCE = 2


class Agent(ABC):
    def __init__(self) -> None:
        self.food_pair = None

    @property
    def has_food(self) -> bool:
        return self.food_pair is not None

    def assign_food(self, food: bool) -> None:
        self.food_pair = food

    def __eq__(self, other: object) -> bool:
        if not isinstance(other, Agent):
            return False

        if self is other:
            return self is other

    @abstractmethod
    def __repr__(self) -> str:
        raise NotImplementedError

    @abstractmethod
    def get_outcome(self, other: list["Agent"]) -> AgentOutcome:
        raise NotImplementedError


class Dove(Agent):
    def __repr__(self) -> str:
        return "Dove"

    def get_outcome(self, others: list[Agent]) -> AgentOutcome:
        """
        Get the outcome of the agent based on the other agents
        """
        if not self.has_food:
            return AgentOutcome.DIE

        # Original simulation

        if not config["agent_overlap"]:
            match others:
                case []:
                    return AgentOutcome.REPRODUCE
                case [Dove()]:
                    return AgentOutcome.SURVIVE
                case [Hawk()]:
                    return np.random.choice([AgentOutcome.SURVIVE, AgentOutcome.DIE])
                case _:
                    raise ValueError(f"Invalid agent type: {others}")

        # Modified simulation

        else:
            if len(others) == 0:
                return AgentOutcome.REPRODUCE

            hawk_count = np.sum(np.vectorize(lambda x: isinstance(x, Hawk))(others))
            dove_count = len(others) - hawk_count + 1
            dove_food = 2 if hawk_count == 0 else 1
            survival_prob = dove_food / dove_count

            choice = np.random.choice(
                [AgentOutcome.SURVIVE, AgentOutcome.DIE],
                p=[survival_prob, 1 - survival_prob],
            )

            return choice


class Hawk(Agent):
    def __init__(self) -> None:
        super().__init__()
        self.wins = 0

    def __repr__(self) -> str:
        return "Hawk"

    def get_outcome(self, others: list[Agent]) -> AgentOutcome:
        """
        Get the outcome of the agent based on the other agents
        """
        if not self.has_food:
            return AgentOutcome.DIE

        # Original simulation

        if not config["agent_overlap"]:
            match others:
                case []:
                    return AgentOutcome.REPRODUCE
                case [Dove()]:
                    return np.random.choice(
                        [AgentOutcome.SURVIVE, AgentOutcome.REPRODUCE]
                    )
                case [Hawk()]:
                    return AgentOutcome.DIE
                case _:
                    raise ValueError(f"Invalid agent type {others}")

        # Modified simulation

        else:
            if len(others) == 0:
                return AgentOutcome.REPRODUCE

            is_hawk_arr = np.vectorize(lambda x: isinstance(x, Hawk))(others)
            hawks = np.array(others)[is_hawk_arr]

            if hawks.size == 0:
                return AgentOutcome.SURVIVE

            else:

                hawk_wins = np.vectorize(lambda x: x.wins)(hawks)
                self_is_best = self.wins >= np.max(hawk_wins)
                is_tie = hawk_wins[hawk_wins == self.wins].size > 0

                if self_is_best and not is_tie:
                    return AgentOutcome.SURVIVE
                else:
                    return AgentOutcome.DIE


# ============
# Simulation
# ============


class FoodPair:
    def __init__(self) -> None:
        self.agents = []

    # ============
    # Properties
    # ============

    @property
    def is_full(self) -> bool:
        return len(self.agents) >= 2

    def __repr__(self) -> str:
        return f"FoodPair({self.agents})"

    # ============
    # Helpers
    # ============

    def assign_agent(self, agent: Agent) -> None:
        """
        Assign an agent to this food pair
        """
        agent.assign_food(self)
        self.agents.append(agent)

    def run_step(self, agents: list[Agent]) -> None:
        """
        Update agents based on the outcome of the food pair
        """
        for agent in self.agents:
            other_agents = [a for a in self.agents if a != agent]
            outcome = agent.get_outcome(other_agents)
            self.handle_outcome(agents, outcome, agent)

    def handle_outcome(
        self, agents: list[Agent], outcome: AgentOutcome, agent: Agent
    ) -> None:

        hawks = [a for a in agents if isinstance(a, Hawk)]
        wins = [h.wins for h in hawks]

        match outcome:
            case AgentOutcome.DIE:
                agents.remove(agent)

            case AgentOutcome.SURVIVE:
                if config["agent_overlap"] and isinstance(agent, Hawk):
                    agent.wins += 1
                pass

            case AgentOutcome.REPRODUCE:
                cls = type(agent)
                agents.append(cls())

            case _:
                raise ValueError("Invalid outcome")


class Simulation:
    def __init__(
        self, num_steps: int, num_food_pairs: int, agents: list[Agent]
    ) -> None:
        self.num_steps = num_steps
        self.num_food_pairs = num_food_pairs
        self.agents = agents
        self.agent_history = []

    # ============
    # Simulation steps
    # ============

    def start(self):
        """
        Start the simulation
        """
        logger.info(f"Starting simulation")

        for i in range(self.num_steps):
            self.run_step(step=i + 1)

    def run_step(self, step) -> None:
        """
        Run a single step of the simulation
        """
        current_count = self.count_agents()
        logger.info(f" [{step:04}]: {current_count}")

        self.agent_history.append(current_count)

        food = np.array([FoodPair() for _ in range(self.num_food_pairs)])
        assigned_Food, starved_agents = self.assign_agents(food)

        self.kill_starved_agents(starved_agents)

        for food_pair in assigned_Food:
            food_pair.run_step(self.agents)

    # ============
    # Helper methods
    # ============

    def kill_starved_agents(self, starved_agents) -> None:
        """
        Remove starved agents from the simulation
        """
        for agent in starved_agents:
            self.agents.remove(agent)

    def assign_agents(self, food: np.ndarray) -> np.ndarray:
        """
        Assign agents to food pairs
        """
        agents_left = self.agents.copy()
        agent_overlap = config["agent_overlap"]

        while len(agents_left) > 0:
            # Check if all food pairs are full and stop searching if so
            if not agent_overlap and np.all(np.vectorize(lambda x: x.is_full)(food)):
                break

            choice: FoodPair = np.random.choice(food)

            if agent_overlap or not choice.is_full:
                choice.assign_agent(agents_left.pop())

        return food, agents_left

    def count_agents(self):
        """
        Count the number of each agent type
        """
        agents_str = np.vectorize(lambda x: str(x))(self.agents)
        count = np.unique(agents_str, return_counts=True)
        return dict(zip(*count))

    # ============
    # Plotting
    # ============

    def plot_agent_history(self) -> None:
        counts = np.array([list(agents.values()) for agents in self.agent_history])

        plt.stackplot(
            range(len(self.agent_history)),
            *counts.T,
            labels=list(self.agent_history[0].keys()),
            colors=["#3f7ea0", "#d53b50"],
        )
        plt.legend(loc="upper left")
        plt.show()


# ============
# Main
# ============


def logging_setup(verbose: bool) -> None:
    logger.setLevel(level=logging.DEBUG if verbose else logging.INFO)


if __name__ == "__main__":

    parser = ArgumentParser()

    # ============
    # Simulation
    # ============

    parser.add_argument(
        "-s",
        "--steps",
        type=int,
        default=100,
        help="Steps to run the simulation for",
    )

    parser.add_argument(
        "-f",
        "--food",
        type=int,
        default=60,
        help="Food pairs to spawn in each step of the simulation",
    )

    parser.add_argument(
        "-a",
        "--agents",
        type=int,
        default=10,
        help="Agents to start the simulation with",
    )

    parser.add_argument(
        "-d",
        "--hawk",
        type=int,
        default=1,
        help="Hawks to start the simulation with",
    )

    parser.add_argument(
        "-o",
        "--agent-overlap",
        action="store_true",
        help="Allow more than 2 agents to select the same food pair",
    )

    parser.add_argument(
        "-p",
        "--plot",
        action="store_true",
        help="Plot the agent history after the simulation",
    )

    # ============
    # Utility
    # ============

    parser.add_argument(
        "-v", "--verbose", action="store_true", help="Print verbose output"
    )

    # ============
    # Run
    # ============

    args = parser.parse_args()
    logging_setup(args.verbose)

    logging.info(f"Running simulation with args: {vars(args)}")

    if args.hawk > args.agents:
        raise ValueError("Cannot have more hawks than agents")

    agents: list[Agent] = [Dove() for _ in range(args.agents - args.hawk)]
    agents.extend([Hawk() for _ in range(args.hawk)])
    shuffle(agents)

    config["agent_overlap"] = args.agent_overlap

    simulation = Simulation(args.steps, args.food, agents)
    simulation.start()

    if args.plot:
        simulation.plot_agent_history()
