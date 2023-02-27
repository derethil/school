import math
from abc import ABC, abstractmethod
from argparse import ArgumentParser

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

"""
===============================================================================
Question 1: Optimal Stopping - Job Candidates
===============================================================================
"""


def report_progress(experiment, iterations):
    """
    Print the progress of an experiment
    """
    if experiment % (iterations / 100) == 0:
        percent = experiment / iterations * 100
        print(f"Running... {percent:.0f}%", end="\r", flush=True)


def plot_percent_found(optimal_found, iterations):
    """
    Plot the percent of optimal solutions found at each stopping point
    """
    percent_found = optimal_found / iterations

    plt.plot(percent_found)
    plt.xlabel("Stopping Point")
    plt.ylabel("Percent Optimal Solutions Found")
    plt.show()


def latest_candidates(candidate, choice, *, rejection):
    """
    Return the latest 5 candidates, or fewer if at the beginning of the list, with rejection
    """
    end = choice + 1
    latest = candidate[0:end] if choice < 5 else candidate[choice - 4 : end]

    if rejection == "half":
        will_reject = (np.random.uniform(0, 1, size=len(latest)) < 0.5).astype(int)
        latest = np.copy(latest) * will_reject

    elif rejection == "increasing":
        probs = np.array([0.8, 0.65, 0.5, 0.35, 0.2])[-len(latest) :]
        will_reject = (np.random.uniform(0, 1, size=len(latest)) < probs).astype(int)
        latest = np.copy(latest) * will_reject

    return latest


def job_candidates(*, len_candidate, iterations, plot, rejection):
    """
    Run the job candidate experiment
    """
    rejection_str = "no" if rejection is None else rejection
    print(f"Question 1: Job Candidates ({rejection_str} rejection)")

    optimal_found = np.zeros(len_candidate)

    for experiment in range(iterations):
        candidate = np.random.uniform(1, 1001, size=len_candidate)
        optimal_choice = np.argmax(candidate)

        for stop in range(1, len_candidate):
            first_choice = np.argmax(candidate[stop:] > max(candidate[0:stop])) + stop

            previous_5 = latest_candidates(candidate, first_choice, rejection=rejection)
            choice = np.argmax(previous_5) + np.where(candidate == previous_5[0])

            if choice.size > 0 and choice == optimal_choice:
                optimal_found[stop] += 1

        report_progress(experiment, iterations)

    print(f"Optimal Stop: {(np.argmax(optimal_found) / len_candidate) * 100:.2f}%")
    if plot:
        plot_percent_found(optimal_found, iterations)


"""
===============================================================================
Question 2: Explore and Exploit - Moving Sensors
===============================================================================
"""


class Environment:
    """
    Environment that returns rewards for each action
    """

    def __init__(self, baseline=False):
        self.num_actions = 5
        self.baseline = baseline

    def reward(self, action):
        rewards = self.gen_rewards()

        reward = rewards[action]

        if self.baseline:
            baseline = np.max(np.random.normal(1.5, 3), 0)

            if reward < baseline:
                reward = 0

        return reward

    def gen_rewards(self):
        rewards = np.array(
            [
                np.random.beta(7, 3) + 2,
                np.random.uniform(0, 4),
                np.random.beta(3, 7) + 2,
                np.random.normal(2, 1.4),
                np.random.normal(1.3, 7),
            ]
        )

        return rewards


class Agent:
    """
    Agent that uses epsilon greedy to choose actions
    """

    def __init__(self, num_actions, eps):
        self.num_actions = num_actions
        self.eps = eps
        self.n = np.zeros(num_actions)
        self.Q = np.zeros(num_actions)

    def update_Q(self, action, reward):
        if reward == 0:
            return

        self.n[action] += 1
        self.Q[action] += (1.0 / self.n[action]) * (reward - self.Q[action])

    def choose_action(self, environment):
        if np.random.random() < self.eps:
            action = np.random.randint(self.num_actions)
        else:
            action = np.argmax(self.Q)

        reward = environment.reward(action)
        return action, reward

    def estimated_reward(self, action):
        return self.Q[action]

    @property
    def estimated_best_reward(self):
        return self.Q.max()


class EpsilonGreedy:
    """
    Run the epsilon greedy experiment
    """

    def __init__(self, eps, num_experiments, num_pulls, drift=False, baseline=False):
        self.eps = eps
        self.num_experiments = num_experiments
        self.num_pulls = num_pulls
        self.drift = drift
        self.baseline = baseline

    def experiment(self):
        """
        Run a single experiment
        """
        env = Environment(self.baseline)
        agent = Agent(env.num_actions, self.eps)
        actions, rewards, estimated = [], [], []

        for _ in range(self.num_pulls):
            action, reward = agent.choose_action(env)
            agent.update_Q(action, reward)

            if reward != 0:
                actions.append(action)
                rewards.append(reward)
                estimated.append(agent.estimated_best_reward)

            else:
                first = len(actions) == 0 or len(rewards) == 0 or len(estimated) == 0
                estimated_from_action = agent.estimated_reward(action)
                actions.append(actions[-1] if not first else action)
                rewards.append(rewards[-1] if not first else reward)
                estimated.append(estimated[-1] if not first else estimated_from_action)

        return (
            np.array(actions, dtype=float),
            np.array(rewards, dtype=float),
            np.array(estimated, dtype=float),
            agent.Q,
        )

    def run(self):
        """
        Run the algorithm for the specified number of experiments
        """
        env = Environment(self.baseline)
        R = np.zeros((self.num_pulls,))
        Q = np.zeros((self.num_pulls,))

        Q_all = np.zeros(env.num_actions)

        for i in range(self.num_experiments):
            _, rewards, estimated, agentQ = self.experiment()

            R += rewards
            Q += estimated

            Q_all += np.array(agentQ)

            report_progress(i, self.num_experiments)

        return (
            R / self.num_experiments,
            Q / self.num_experiments,
            Q_all / self.num_experiments,
        )


class Experiment:
    """
    Run the epsilon greedy experiment for a range of epsilons
    """

    def __init__(
        self,
        epsilons,
        num_experiments=1,
        num_pulls=365,
        drift=False,
        baseline=False,
    ):
        self.epsilons = epsilons

        self.num_experiments = num_experiments
        self.num_pulls = num_pulls
        self.drift = drift
        self.baseline = baseline

    def run(self, plot=False):
        """
        Run the experiment for a range of epsilons
        """
        convergences = self.find_convergences()

        for eps, (average, best, actions) in zip(self.epsilons, convergences):
            print(actions)
            print(
                f"[Epsilon Greedy] "
                + f"eps = {eps:.2f} | "
                + f"experiments = {self.num_experiments} | "
                + f"pulls = {self.num_pulls} | "
                + f"avg_reward = {np.mean(average):.4f} | "
                f"best_reward_estimate = {np.mean(average):.4f}"
            )

            if plot:
                plt.plot(best, label=f"$\\epsilon$={eps:.4f}")

        if plot:
            self.show_plot("Epsilon Greedy")

    def find_convergences(self):
        """
        Find the convergences for a range of epsilons
        """
        estimated_convergences = []

        for eps in self.epsilons:
            algorithm = EpsilonGreedy(
                eps, self.num_experiments, self.num_pulls, self.drift, self.baseline
            )
            result = algorithm.run()
            estimated_convergences.append(result)

        return estimated_convergences

    def show_plot(self, title):
        """
        Show the plot of the convergences
        """
        plt.title(f"Convergence of {title}")
        plt.xlabel("Days")
        plt.ylabel("Best Estimated Reward")
        plt.legend()

        plt.grid()
        plt.show()


def run_epsilon(epsilons, plot=False, baseline=False):
    """
    Run the epsilon greedy algorithm with the given epsilons
    """
    print("Question 2: Moving Sensors")
    experiment = Experiment(epsilons, num_experiments=100, baseline=baseline)
    experiment.run(plot)
    return experiment


def actual_rewards(num=1000000, plot=False, baseline=False):
    """
    Find the actual rewards by testing each sensor many, many times
    """
    print("Question 2: Moving Sensors")
    env = Environment(baseline=baseline)

    rewards = np.zeros(env.num_actions)
    for _ in range(num):
        all_rewards = env.gen_rewards()
        rewards += all_rewards

    rewards /= num

    print("Actual Sensor Readings:")
    for i, reward in enumerate(rewards):
        print(f"Sensor {i}: {reward:.2f}")

    if plot:
        plt.bar(range(env.num_actions), rewards)
        plt.title("Actual Sensor Readings")
        plt.xlabel("Sensor")
        plt.ylabel("Readings")
        plt.show()


"""
===============================================================================
Question 3: American Options - Franchises
===============================================================================
"""


class Contract(ABC):
    def __init__(self, initial):
        self.initial = initial

    @abstractmethod
    def get_payoff(self, paths):
        raise NotImplementedError


class EuropeanContract(Contract):
    def get_payoff(self, paths):
        return np.maximum(paths[:, -1] - self.initial, 0)


class AmericanContract(Contract):
    def __init__(self, initial, strike):
        super().__init__(initial)
        self.strike = strike

    def get_payoff(self, paths):
        first_greater_idx = np.argmax(paths > self.strike, axis=1)
        first_greater = paths[np.arange(paths.shape[0]), first_greater_idx]
        return np.maximum(first_greater - self.initial, 0)


class Simulation:
    def __init__(self, initial_price, drift_percent, volatility_percent, dt, T):
        # Model Parameters
        self.current_price = initial_price
        self.initial_price = initial_price
        self.drift_percent = drift_percent
        self.volatility_percent = volatility_percent
        # Time Parameters
        self.dt = dt
        self.T = T
        # Price Path
        self.prices = []

    @property
    def drift(self):
        return self.current_price * self.drift_percent

    def volatility(self, T, volatility_change=False):
        if volatility_change and T < 0.5:
            return self.current_price * 0.19

        return self.current_price * self.volatility_percent

    def simulate_path(self, volatility_change=False):
        T = self.T
        while T - self.dt > 0:
            dWt = np.random.normal(0, math.sqrt(self.dt))
            dYt = self.drift + self.volatility(T, volatility_change) * dWt
            self.current_price += dYt
            self.prices.append(self.current_price)
            T -= self.dt

        return self.prices


class Model:
    def __init__(self, num_paths, initial_price, drift, volatility, steps) -> None:
        self.num_paths = num_paths
        self.price_paths: list[int] = []

        self.initial_price = initial_price
        self.drift = drift
        self.volatility = volatility
        self.T = 1
        self.dt = self.T / steps

    def generate_paths(self, volatility_change=False):
        for _ in range(self.num_paths):
            sim = Simulation(
                self.initial_price, self.drift, self.volatility, self.dt, self.T
            )
            price_path = sim.simulate_path(volatility_change=volatility_change)
            self.price_paths.append(price_path)

        return np.array(self.price_paths)


def contract_value(num_paths, show_plot, contract_type, volatility_change):
    print("Question 3: American Options - Franchises")
    surge_str = "volatility change" if volatility_change else "no volatility change"
    print(
        f"Running {num_paths} simulations with {contract_type.title()} contract (with {surge_str})..."
    )
    initial_price = 385
    model = Model(num_paths, initial_price, 0.04 / 12, 0.03, 120)
    paths = model.generate_paths(volatility_change=volatility_change)

    contract = (
        EuropeanContract(initial_price)
        if contract_type == "european"
        else AmericanContract(initial_price, 535)
    )
    payoffs = contract.get_payoff(paths)

    print(f"  Initial Value: {model.initial_price:.2f}")
    print(f"  Average Final Value: {np.mean(paths[:, -1]):.2f}")
    print(f"  Contract Value: {np.mean(payoffs):.2f}")

    if show_plot:
        for path in paths:
            plt.plot(path)
        plt.show()


"""
===============================================================================
Main Program
===============================================================================
"""


if __name__ == "__main__":
    # np.random.seed(122)  # seed for reproducibility

    parser = ArgumentParser()

    # Base arguments

    parser.add_argument(
        "question", help="Question 1, 2, or 3", type=int, choices=[1, 2, 3]
    )
    parser.add_argument("--plot", help="Plot the results", action="store_true")

    # Question 1 arguments

    parser.add_argument(
        "--rejection",
        help="Use rejection (question 1 only)",
        choices=["half", "increasing"],
    )

    # Question 2 arguments

    parser.add_argument(
        "--epsilons",
        help="Epsilon values to use (question 2 only)",
        nargs="+",
        type=float,
        default=[0.09],
    )

    parser.add_argument(
        "--baseline",
        help="Use the baseline sensor (question 2 only)",
        action="store_true",
    )

    parser.add_argument(
        "--actual-rewards",
        help="Find the actual rewards (question 2 only)",
        action="store_true",
    )

    # Question 3 arguments

    parser.add_argument(
        "--type",
        help="Type of contract (question 3 only)",
        choices=["european", "american"],
        default="american",
    )

    parser.add_argument(
        "--volatility-change",
        help="Add a volatility surge to 19 at year 5 (question 3 only)",
        action="store_true",
    )

    # Parse arguments

    args = parser.parse_args()

    match vars(args):
        # Question 1: Job Candidates

        case {"question": 1, "plot": should_plot, "rejection": rejection}:
            job_candidates(
                len_candidate=100,
                iterations=100000,
                plot=should_plot,
                rejection=rejection,
            )

        # Question 2: Moving Sensors

        case {
            "question": 2,
            "plot": should_plot,
            "epsilons": epsilons,
            "actual_rewards": False,
            "baseline": baseline,
        }:
            run_epsilon(epsilons, plot=should_plot, baseline=baseline)

        case {
            "question": 2,
            "actual_rewards": True,
            "plot": plot,
            "baseline": baseline,
        }:
            actual_rewards(plot=plot, baseline=baseline)

        # Question 3: American Options - Franchises

        case {
            "question": 3,
            "plot": should_plot,
            "type": contract_type,
            "volatility_change": volatility_change,
        }:
            contract_value(5000, should_plot, contract_type, volatility_change)

        # Invalid
        case _:
            parser.print_usage()
