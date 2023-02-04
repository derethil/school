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
        shift = self.shifts * (current_pull // 3000) if current_pull is not None else 0
        return np.random.normal(self.means + drift + shift, self.std_devs)


# Agent
# Handles the logic for choosing an action


class Agent(ABC):
    def __init__(self, num_actions):
        self.num_actions = num_actions

    @abstractmethod
    def choose_action(self, environment):
        raise NotImplementedError

    @property
    def estimated_best_reward(self):
        raise NotImplementedError


class EpsilonGreedyAgent(Agent):
    def __init__(self, num_actions, eps):
        super().__init__(num_actions)
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


class ThompsonSamplingAgent(Agent):
    def __init__(self, num_actions, init_As=1, init_Bs=1):
        super().__init__(num_actions)
        self.As = np.full(num_actions, init_As, dtype=np.float64)
        self.Bs = np.full(num_actions, init_Bs, dtype=np.float64)

        self.reward_As = np.full(num_actions, init_As, dtype=np.float64)
        self.reward_Bs = np.full(num_actions, init_Bs, dtype=np.float64)

    def choose_action(self, environment, current_pull=None):
        action = np.argmax(np.random.beta(self.As, self.Bs))
        reward = environment.reward(action, current_pull)
        sigmoided = 1 / (1 + np.exp(-reward))

        self.As[action] += sigmoided
        self.Bs[action] += 1 - sigmoided

        self.reward_As[action] += reward
        self.reward_Bs[action] += 1 - reward

        return action, reward

    @property
    def estimated_best_reward(self):
        return np.max(self.reward_As / (self.reward_As + self.reward_Bs))


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


class ThomsonSampling(Solver):
    def experiment(self):
        env = Environment()
        agent = ThompsonSamplingAgent(env.num_actions)
        actions, rewards, estimated = [], [], []

        for pull in range(self.num_pulls):
            action, reward = agent.choose_action(env, pull if self.drift else None)
            reward = env.reward(action)
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

    def show_plot(self, title):
        plt.title(f"Convergence of {title}")
        plt.xlabel("Pulls")
        plt.ylabel("Best Estimated Reward")
        plt.legend()
        # plt.ylim(1.5, 2.5)
        plt.grid()
        plt.show()


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


class ExperimentThompson(Experiment):
    def find_convergences(self):
        algorithm = ThomsonSampling(self.num_experiments, self.num_pulls, self.drift)
        result = algorithm.run(self.verbose)
        return [result]

    def plot_convergences(self):
        convergences = self.find_convergences()

        for average, best in convergences:
            plt.plot(best, label="Thompson Sampling")
            print(
                f"[Thompson Sampling] "
                + f"experiments = {self.num_experiments} | "
                + f"pulls = {self.num_pulls} | "
                + f"avg_reward = {np.mean(average):.4f} | "
                f"best_reward_estimate = {np.mean(best):.4f}"
            )


# Main CLI

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "algorithm",
        help="algorithm to perform experiments with",
        choices=["epsilon_greedy", "thompson_sampling", "all"],
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

    args = parser.parse_args()

    match vars(args):
        case {"algorithm": "epsilon_greedy"}:
            experiment_epsilon = ExperimentEpsilon(
                args.epsilons,
                num_experiments=10,
                verbose=args.verbose,
                drift=args.drift,
            )
            experiment_epsilon.plot_convergences()
            experiment_epsilon.show_plot("Epsilon-Greedy")

        case {"algorithm": "thompson_sampling"}:
            experiment_thompson = ExperimentThompson(
                num_experiments=10, verbose=args.verbose, drift=args.drift
            )
            experiment_thompson.plot_convergences()
            experiment_thompson.show_plot("Thompson Sampling")

        case {"algorithm": "all"}:
            experiment_epsilon = ExperimentEpsilon(
                args.epsilons,
                num_experiments=10,
                verbose=args.verbose,
                drift=args.drift,
            )

            experiment_thompson = ExperimentThompson(
                num_experiments=10, verbose=args.verbose, drift=args.drift
            )

            experiment_epsilon.plot_convergences()
            experiment_thompson.plot_convergences()

            experiment_epsilon.show_plot("All Algorithms Comparison")

        case _:
            parser.print_usage()
