from abc import ABC, abstractmethod
from argparse import ArgumentParser
from enum import Enum
from types import NoneType
from typing import Optional

import numpy as np
import matplotlib.pyplot as plt


# ============
# Agents
# ============


class AgentOutcome(Enum):
    DIE = 0
    SURVIVE = 1
    REPRODUCE = 2


class Agent(ABC):
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
        print(type(other))
        match type(other):
            case NoneType():
                return AgentOutcome.SURVIVE
            case Dove():
                return AgentOutcome.SURVIVE
            case Hawk():
                return AgentOutcome.SURVIVE
            case agent_type:
                raise ValueError(f"Invalid agent type {agent_type}")


class Hawk(Agent):
    def __repr__(self) -> str:
        return "Hawk"

    def get_outcome(self, other: Optional[Agent]) -> AgentOutcome:
        match type(other):
            case NoneType():
                return AgentOutcome.SURVIVE
            case Dove():
                return AgentOutcome.SURVIVE
            case Hawk():
                return AgentOutcome.SURVIVE
            case agent_type:
                raise ValueError(f"Invalid agent type {agent_type}")


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
        elif self.agent2 is None:
            self.agent2 = agent
        else:
            raise ValueError("FoodPair already has 2 agents")

    def run_step(self, agents: list[Agent]) -> None:
        print(f"Running step for {self}")
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
                pass
            case AgentOutcome.REPRODUCE:
                pass
            case _:
                raise ValueError("Invalid outcome")


class Simulation:
    def __init__(
        self, num_steps: int, num_food_pairs: int, agents: list[Agent]
    ) -> None:
        self.num_steps = num_steps
        self.num_food_pairs = num_food_pairs
        self.agents = agents

    def start(self):
        self.run_step()

    def assign_agents(self, food: np.ndarray) -> np.ndarray:
        agents_left = self.agents.copy()

        while len(agents_left) > 0:
            choice: FoodPair = np.random.choice(food)

            if not choice.is_full:
                choice.assign_agent(agents_left.pop())

        return food

    def run_step(self) -> None:
        food = np.array([FoodPair() for _ in range(self.num_food_pairs)])
        assigned_Food = self.assign_agents(food)

        np.vectorize(FoodPair.run_step)(assigned_Food, self.agents)


# ============
# Main
# ============

if __name__ == "__main__":
    parser = ArgumentParser()

    parser.add_argument(
        "--num-steps",
        type=int,
        default=10,
        help="Number of steps to run the simulation for",
    )

    parser.add_argument(
        "--num-food-pairs",
        type=int,
        default=10,
        help="Number of food pairs to spawn in each step of the simulation",
    )

    args = parser.parse_args()

    agents: list[Agent] = [Dove() for _ in range(10)]
    simulation = Simulation(args.num_steps, args.num_food_pairs, agents)
    simulation.start()
