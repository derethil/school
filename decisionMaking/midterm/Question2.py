import argparse
from abc import ABC, abstractmethod

import numpy as np
import matplotlib.pyplot as plt

# Environment
# Handles the rewards for each action


class Environment:
    # fmt: off
    means = np.array([0, -0.5, 2, -0.5, -1.2, -3, -10, -0.5, -1, 1, 0.7, -6, -7, -0.5, -6.5, -3, 0, 2, -9, -1, -4.5])
    std_devs = np.array([5, 12, 3.9, 7, 8, 7, 20, 1, 2, 6, 4, 11, 1, 2, 1, 6, 8, 3.9, 12, 6, 8])
    shifts = np.array([5, 0, 2, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0])
    # fmt: on

    def __init__(self):
        self.num_actions = len(self.means)

    def reward(self, action, current_pull=0):
        rewards = self.gen_rewards(current_pull)
        return rewards[action]

    def gen_rewards(self, current_pull=None):
        drift = -0.001 * current_pull if current_pull is not None else 0
        shift = self.shifts if current_pull and current_pull >= 3000 else 0

        return np.random.normal(self.means + drift + shift, self.std_devs)


# Agent
# Handles the logic for choosing an action


class EpsilonGreedyAgent:
    def __init__(self, num_actions, eps):
        self.num_actions = num_actions
        self.eps = eps
        self.n = np.zeros(num_actions)
        self.Q = np.zeros(num_actions)

    def update_Q(self, action, reward):
        self.n[action] += 1
        self.Q[action] += (1.0 / self.n[action]) * (reward - self.Q[action])

    def choose_action(self, environment, current_pull=None):
        if np.random.random() < self.eps:
            action = np.random.randint(self.num_actions)
        else:
            action = np.random.choice(np.flatnonzero(self.Q == self.Q.max()))

        reward = environment.reward(action, current_pull)
        return action, reward

    @property
    def estimated_best_reward(self):
        return self.Q.max()


# Solver
# Handles the logic for running an algorithm


class Solver(ABC):
    def __init__(self, num_experiments, num_pulls, drift=False):
        self.num_experiments = num_experiments
        self.num_pulls = num_pulls
        self.drift = drift

    @abstractmethod
    def experiment(self):
        raise NotImplementedError

    def run(self, verbose=False):
        if verbose:
            print(f"Running {self.__class__.__name__}...")

        env = Environment()

        R = np.zeros((self.num_pulls,))
        Q = np.zeros((self.num_pulls,))
        A = np.zeros((self.num_pulls, env.num_actions))

        for i in range(self.num_experiments):
            actions, rewards, estimated = self.experiment()

            fraction = self.num_experiments / 1
            if verbose and ((i + 1) % (self.num_experiments / fraction) == 0):
                eps_str = f"| eps = {self.eps:.2f} " if hasattr(self, "eps") else ""
                print(
                    f"[Experiment {i+1:02}/{self.num_experiments:2}] "
                    + f"num_pulls = {self.num_pulls} "
                    + eps_str
                    + f"| avg_reward = {np.mean(rewards):.2f}"
                )

            R += rewards
            Q += estimated
            for j, a in enumerate(actions):
                A[j, a] += 1

        return R / self.num_experiments, Q / self.num_experiments


class EpsilonGreedy(Solver):
    def __init__(self, eps, *args):
        super().__init__(*args)
        self.eps = eps

    def experiment(self):
        env = Environment()
        agent = EpsilonGreedyAgent(env.num_actions, self.eps)
        actions, rewards, estimated = [], [], []

        for pull in range(self.num_pulls):
            action, reward = agent.choose_action(env, pull if self.drift else None)
            agent.update_Q(action, reward)
            actions.append(action)
            rewards.append(reward)
            estimated.append(agent.estimated_best_reward)

        return np.array(actions), np.array(rewards), np.array(estimated)


# Experiment
# Handles the logic for running an experiment


class Experiment(ABC):
    def __init__(self, num_experiments=1, num_pulls=10000, verbose=False, drift=False):
        self.num_experiments = num_experiments
        self.num_pulls = num_pulls
        self.verbose = verbose
        self.drift = drift

    @abstractmethod
    def find_convergences(self):
        raise NotImplementedError

    @abstractmethod
    def plot_convergences(self):
        raise NotImplementedError

    def show_plot(self, title, limit=True):
        plt.title(f"Convergence of {title}")
        plt.xlabel("Pulls")
        plt.ylabel("Best Estimated Reward")
        plt.legend()

        if limit:
            plt.ylim(1.5, 2.5)

        plt.grid()
        plt.show()

    def remove_some_data(self, data, percentage):
        to_skip = int(1 / percentage)
        copy = np.zeros(data.shape)
        copy[::to_skip] = data[::to_skip]
        copy[copy == 0] = np.nan
        return copy


class ExperimentEpsilon(Experiment):
    def __init__(self, epsilons, **kwargs):
        super().__init__(**kwargs)
        self.epsilons = epsilons

    def find_convergences(self):
        estimated_convergences = []

        for eps in self.epsilons:
            algorithm = EpsilonGreedy(
                eps, self.num_experiments, self.num_pulls, self.drift
            )
            result = algorithm.run(self.verbose)
            estimated_convergences.append(result)

        return estimated_convergences

    def plot_convergences(self):
        convergences = self.find_convergences()

        for eps, (average, best) in zip(self.epsilons, convergences):
            plt.plot(best, label=f"$\\epsilon$-Greedy: $\\epsilon$={eps:.2f}")
            print(
                f"[Epsilon Greedy] "
                + f"eps = {eps:.2f} | "
                + f"experiments = {self.num_experiments} | "
                + f"pulls = {self.num_pulls} | "
                + f"avg_reward = {np.mean(average):.4f} | "
                f"best_reward_estimate = {np.mean(best):.4f}"
            )


# Main CLI


def run_epsilon(epsilons, verbose, drift):
    experiment = ExperimentEpsilon(
        epsilons, num_experiments=10, verbose=verbose, drift=drift
    )
    experiment.plot_convergences()

    return experiment


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "algorithm",
        help="algorithm to perform experiments with",
        choices=["epsilon_greedy"],
    )
    parser.add_argument(
        "-v", "--verbose", action="store_true", help="increase output verbosity"
    )

    parser.add_argument(
        "-d", "--drift", action="store_true", help="enable drift in rewards"
    )

    parser.add_argument(
        "-e",
        "--epsilons",
        type=float,
        nargs="+",
        help="list of epsilons to use",
        default=[0.01, 0.05, 0.1, 0.4],
    )

    parser.add_argument(
        "-rt",
        "--restart-thompson",
        action="store_true",
        help="restart thompson sampling at pull 3000",
    )

    args = parser.parse_args()

    match vars(args):
        case {"algorithm": "epsilon_greedy"}:
            e = run_epsilon(args.epsilons, args.verbose, args.drift)
            e.show_plot("Epsilon-Greedy", limit=not args.drift)

        case _:
            parser.print_usage()
