from abc import ABC, abstractmethod
from argparse import ArgumentParser
from enum import Enum
from types import NoneType
from typing import Optional
import logging

import numpy as np
import matplotlib.pyplot as plt


logger = logging.getLogger("game_theory")


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

    @abstractmethod
    def __repr__(self) -> str:
        raise NotImplementedError

    @abstractmethod
    def get_outcome(self, other: Optional["Agent"]) -> AgentOutcome:
        raise NotImplementedError


class Dove(Agent):
    def __repr__(self) -> str:
        return "Dove"

    def get_outcome(self, other: Optional[Agent]) -> AgentOutcome:
        if not self.has_food:
            return AgentOutcome.DIE

        match other:
            case NoneType():
                return AgentOutcome.REPRODUCE
            case Dove():
                return AgentOutcome.SURVIVE
            case Hawk():
                return np.random.choice([AgentOutcome.SURVIVE, AgentOutcome.DIE])
            case _:
                raise ValueError(f"Invalid agent type: {other}")


class Hawk(Agent):
    def __repr__(self) -> str:
        return "Hawk"

    def get_outcome(self, other: Optional[Agent]) -> AgentOutcome:
        if not self.has_food:
            return AgentOutcome.DIE

        match other:
            case NoneType():
                return AgentOutcome.REPRODUCE
            case Dove():
                return np.random.choice([AgentOutcome.SURVIVE, AgentOutcome.REPRODUCE])
            case Hawk():
                return AgentOutcome.DIE
            case _:
                raise ValueError(f"Invalid agent type {other}")


# ============
# Simulation
# ============


class FoodPair:
    def __init__(self) -> None:
        self.agent1: Optional[Agent] = None
        self.agent2: Optional[Agent] = None

    @property
    def is_full(self) -> bool:
        return self.agent1 is not None and self.agent2 is not None

    def __repr__(self) -> str:
        match self.agent1, self.agent2:
            case (None, None):
                return "FoodPair()"

            case (None, self.agent2) | (self.agent1, None):
                return f"FoodPair({self.agent1 if self.agent1 else self.agent2})"

            case (self.agent1, self.agent2):
                return f"FoodPair({self.agent1}, {self.agent2})"

            case _:
                raise ValueError("Invalid FoodPair")

    def assign_agent(self, agent: Agent) -> None:
        if self.agent1 is None:
            self.agent1 = agent
            self.agent1.assign_food(self)

        elif self.agent2 is None:
            self.agent2 = agent
            self.agent2.assign_food(self)

        else:
            raise ValueError("FoodPair already has 2 agents")

    def run_step(self, agents: list[Agent]) -> None:
        if self.agent1:
            agent1_outcome = self.agent1.get_outcome(self.agent2)
            self.handle_outcome(agents, agent1_outcome, self.agent1)

        if self.agent2:
            agent2_outcome = self.agent2.get_outcome(self.agent1)
            self.handle_outcome(agents, agent2_outcome, self.agent2)

    def handle_outcome(
        self, agents: list[Agent], outcome: AgentOutcome, agent: Agent
    ) -> None:

        match outcome:
            case AgentOutcome.DIE:
                agents.remove(agent)

            case AgentOutcome.SURVIVE:
                # Do nothing
                pass

            case AgentOutcome.REPRODUCE:
                agents.append(agent)

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

    def start(self):
        logger.info(f"Starting simulation")

        for i in range(self.num_steps):
            self.run_step(step=i + 1)

    def run_step(self, step) -> None:
        current_count = self.count_agents()
        logger.info(f" [{step:04}]: {current_count}")
        self.agent_history.append(current_count)
        food = np.array([FoodPair() for _ in range(self.num_food_pairs)])
        assigned_Food, starved_agents = self.assign_agents(food)

        self.kill_starved_agents(starved_agents)

        for food_pair in assigned_Food:
            food_pair.run_step(self.agents)

    def kill_starved_agents(self, starved_agents) -> None:
        for agent in starved_agents:
            self.agents.remove(agent)

    def assign_agents(self, food: np.ndarray) -> np.ndarray:
        agents_left = self.agents.copy()

        while len(agents_left) > 0:
            # Check if all food pairs are full and stop searching if so
            if np.all(np.vectorize(lambda x: x.is_full)(food)):
                break

            choice: FoodPair = np.random.choice(food)

            if not choice.is_full:
                choice.assign_agent(agents_left.pop())

        return food, agents_left

    def count_agents(self):
        agents_str = np.vectorize(lambda x: str(x))(self.agents)
        count = np.unique(agents_str, return_counts=True)
        return dict(zip(*count))

    def plot_agent_history(self) -> None:
        counts = np.array([list(agents.values()) for agents in self.agent_history])

        plt.stackplot(
            range(self.num_steps),
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

    # Simulation arguments

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

    # Utility arguments

    parser.add_argument(
        "-v", "--verbose", action="store_true", help="Print verbose output"
    )

    # Parse arguments and run simulation

    args = parser.parse_args()
    logging_setup(args.verbose)

    logging.info(f"Running simulation with args: {vars(args)}")

    agents: list[Agent] = [Dove() for _ in range(args.agents - args.hawk)]
    agents.extend([Hawk() for _ in range(args.hawk)])

    simulation = Simulation(args.steps, args.food, agents)
    simulation.start()
    simulation.plot_agent_history()
