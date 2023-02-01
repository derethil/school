import numpy as np
import matplotlib.pyplot as plt

def rewards(drift=0):
    return [
        np.random.normal(0, 5),
        np.random.normal(-0.5,12),
        np.random.normal(2,3.9),
        np.random.normal(-0.5,7),
        np.random.normal(-1.2,8),
        np.random.normal(-3,7),
        np.random.normal(-10,20),
        np.random.normal(-0.5,1),
        np.random.normal(-1,2),
        np.random.normal(1,6),
        np.random.normal(0.7,4),
        np.random.normal(-6,11),
        np.random.normal(-7,1),
        np.random.normal(-0.5,2),
        np.random.normal(-6.5,1),
        np.random.normal(-3,6),
        np.random.normal(0,8),
        np.random.normal(2,3.9),
        np.random.normal(-9,12),
        np.random.normal(-1,6),
        np.random.normal(-4.5,8)
    ]

class Environment:
    def __init__(self, rewards):
        self.rewards = rewards

    def reward(self, action):
        rewards = self.rewards()
        return rewards[action]

class Agent():
    def __init__(self, nActions, eps):
        self.nActions = nActions
        self.eps = eps
        self.n = np.zeros(nActions)#, dtype=np.int) # action counts n(a)
        self.Q = np.zeros(nActions)#, dtype=np.float) # value Q(a)

    def update_Q(self, action, reward):
        # Update Q action-value given (action, reward)
        self.n[action] += 1
        self.Q[action] += (1.0/self.n[action]) * (reward - self.Q[action])

    def get_action(self):
        # Epsilon-greedy policy
        if np.random.random() < self.eps: # explore
            return np.random.randint(self.nActions)
        else: # exploit
            return np.random.choice(np.flatnonzero(self.Q == self.Q.max()))

class EpsilonGreedy():
    def __init__(self, eps, num_experiments = 1000, num_pulls = 500):
        self.eps = eps
        self.num_experiments = num_experiments
        self.num_pulls = num_pulls
        self.rewards = rewards

    def experiment(self):
        env = Environment(self.rewards)
        agent = Agent(len(env.rewards()), self.eps)
        actions, rewards = [], []

        for _ in range(self.num_pulls):
            action = agent.get_action()
            reward = env.reward(action)
            agent.update_Q(action, reward)
            actions.append(action)
            rewards.append(reward)

        return np.array(actions), np.array(rewards)

    def run(self, verbose=False):
        R = np.zeros((self.num_pulls,))
        A = np.zeros((self.num_pulls, len(self.rewards())))

        for i in range(self.num_experiments):
            actions, rewards = self.experiment()

            if verbose and ((i + 1) % (self.num_experiments / (self.num_experiments / 10)) == 0):
                print(f"[Experiment {i+1}/{self.num_experiments}] num_pulls = {self.num_pulls} | eps = {self.eps} | avg_reward = {np.mean(rewards):.2f}")

            R += rewards
            for j, a in enumerate(actions):
                A[j, a] += 1

        return (R / self.num_experiments, np.mean(rewards))

def plot_convergence(action_rewards, num_pulls):
    plt.plot(action_rewards, ".")
    plt.xlabel("Pulls")
    plt.ylabel("Average Reward")
    plt.grid()
    plt.xlim([0, num_pulls])
    plt.show()

def find_convergence(eps, verbose=False):
    alg = EpsilonGreedy(eps, num_experiments=5, num_pulls=10000)
    rewards, average_reward = alg.run(verbose)

    print(f"[eps={eps:.2f}] average_reward = {np.mean(average_reward):.4f}")
    return average_reward

if __name__ == "__main__":
    find_convergence(0.01, verbose=False)
    find_convergence(0.05, verbose=False)
    find_convergence(0.1, verbose=False)
    find_convergence(0.4, verbose=False)