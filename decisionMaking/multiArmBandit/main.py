import sys
from abc import ABC, abstractmethod

import numpy as np
import matplotlib.pyplot as plt


class Environment:
    def __init__(self, rewards):
        self.rewards = rewards

    def reward(self, action):
        rewards = self.rewards()
        return rewards[action]


class Agent(ABC):
    def __init__(self, num_actions):
        self.num_actions = num_actions

    @abstractmethod
    def choose_action(self):
        raise NotImplementedError

    @property
    def estimated_reward(self):
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

    def choose_action(self):
        if np.random.random() < self.eps:
            return np.random.randint(self.num_actions)
        else:
            return np.random.choice(np.flatnonzero(self.Q == self.Q.max()))

    @property
    def estimated_reward(self):
        return self.Q.max()


class ThompsonSamplingAgent(Agent):
    def __init__(self, num_actions, init_As=1, init_Bs=1):
        super().__init__(num_actions)
        self.As = np.full(num_actions, init_As, dtype=np.float64)
        self.Bs = np.full(num_actions, init_Bs, dtype=np.float64)

    def choose_action(self, environment):
        action = np.argmax(
            [np.random.beta(self.As[i], self.Bs[i]) for i in range(self.num_actions)]
        )

        reward = environment.reward(action)

        print(f"pulled lever {action} for {reward}")

        self.As[action] += reward
        self.Bs[action] += 1 - reward

        return action

    @property
    def estimated_reward(self):
        return np.max(self.As / (self.As + self.Bs))


class Solver(ABC):
    def rewards(self, drift=0):
        return [
            np.random.normal(0, 5),
            np.random.normal(-0.5, 12),
            np.random.normal(2, 3.9),
            np.random.normal(-0.5, 7),
            np.random.normal(-1.2, 8),
            np.random.normal(-3, 7),
            np.random.normal(-10, 20),
            np.random.normal(-0.5, 1),
            np.random.normal(-1, 2),
            np.random.normal(1, 6),
            np.random.normal(0.7, 4),
            np.random.normal(-6, 11),
            np.random.normal(-7, 1),
            np.random.normal(-0.5, 2),
            np.random.normal(-6.5, 1),
            np.random.normal(-3, 6),
            np.random.normal(0, 8),
            np.random.normal(2, 3.9),
            np.random.normal(-9, 12),
            np.random.normal(-1, 6),
            np.random.normal(-4.5, 8),
        ]

    def __init__(self, eps, num_experiments, num_pulls):
        self.eps = eps
        self.num_experiments = num_experiments
        self.num_pulls = num_pulls

    @abstractmethod
    def experiment(self):
        raise NotImplementedError

    def run(self, verbose=False):
        R = np.zeros((self.num_pulls,))
        Q = np.zeros((self.num_pulls,))
        A = np.zeros((self.num_pulls, len(self.rewards())))

        for i in range(self.num_experiments):
            actions, rewards, estimated = self.experiment()

            fraction = self.num_experiments / 1
            if verbose and ((i + 1) % (self.num_experiments / fraction) == 0):
                print(
                    f"[Experiment {i+1}/{self.num_experiments}] "
                    + f"num_pulls = {self.num_pulls} | eps = {self.eps} "
                    + f"| avg_reward = {np.mean(rewards):.2f}"
                )

            R += rewards
            Q += estimated
            for j, a in enumerate(actions):
                A[j, a] += 1

        return R / self.num_experiments, Q / self.num_experiments


class EpsilonGreedy(Solver):
    def experiment(self):
        env = Environment(self.rewards)
        agent = EpsilonGreedyAgent(len(env.rewards()), self.eps)
        actions, rewards, estimated = [], [], []

        for _ in range(self.num_pulls):
            action = agent.choose_action()
            reward = env.reward(action)
            agent.update_Q(action, reward)
            actions.append(action)
            rewards.append(reward)
            estimated.append(agent.estimated_reward)

        return np.array(actions), np.array(rewards), np.array(estimated)


class ThomsonSampling(Solver):
    def experiment(self):
        env = Environment(self.rewards)
        agent = ThompsonSamplingAgent(len(env.rewards()))
        actions, rewards, estimated = [], [], []

        for _ in range(self.num_pulls):
            action = agent.choose_action(env)
            reward = env.reward(action)
            actions.append(action)
            rewards.append(reward)
            estimated.append(agent.estimated_reward)

        return np.array(actions), np.array(rewards), np.array(estimated)


def find_convergence(eps, type, verbose=False):
    algorithm_cls = EpsilonGreedy if type == "epsilon" else ThomsonSampling
    algorithm = algorithm_cls(eps, num_experiments=1, num_pulls=10000)

    averaged_rewards, estimated_rewards = algorithm.run(verbose)
    print(
        f"[eps={eps:.2f}] average_reward = {np.mean(averaged_rewards):.4f}, "
        + f"estimated_reward = {np.mean(estimated_rewards):.4f}"
    )

    return averaged_rewards, estimated_rewards


def compare_convergences(epsilons, type, verbose=False):
    for eps in epsilons:
        rewards, estimated = find_convergence(eps, type, verbose)
        plt.plot(estimated, label=f"eps={eps:.2f}")

    plt.title("Convergence of Epsilon-Greedy")
    plt.xlabel("Pulls")
    plt.ylabel("Estimated Reward")
    plt.ylim(1.5, 2.5)
    plt.legend()
    plt.grid()
    plt.show()


if __name__ == "__main__":
    epsilons = [0.01, 0.05, 0.1, 0.4]
    match sys.argv[1:]:
        case ["epsilon_greedy"]:
            compare_convergences(epsilons, "epsilon", verbose=False)
        case ["thompson_sampling"]:
            compare_convergences(epsilons, "thomson", verbose=False)
        case _:
            print("Usage: python3 bandit.py epsilon_greedy|thomson_sampling")
