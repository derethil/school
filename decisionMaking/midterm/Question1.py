import sys
import matplotlib.pyplot as plt
from argparse import ArgumentParser

import numpy as np

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


class EpsilonGreedyAgent:
    """
    Agent that uses epsilon greedy to choose actions
    """

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


class EpsilonGreedy:
    """
    Run the epsilon greedy experiment
    """

    def __init__(self, eps, num_experiments, num_pulls, drift=False, *args):
        self.eps = eps
        self.num_experiments = num_experiments
        self.num_pulls = num_pulls
        self.drift = drift

    def experiment(self):
        """
        Run a single experiment
        """
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

    def run(self, verbose=False):
        """
        Run the algorithm for the specified number of experiments
        """
        if verbose:
            print(f"Running Epsilon Greedy with epsilon={self.eps}...")

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
                    + f"| avg_reward = {np.mean(rewards):.2f}"
                )

            R += rewards
            Q += estimated
            for j, a in enumerate(actions):
                A[j, a] += 1

        return R / self.num_experiments, Q / self.num_experiments


class ExperimentEpsilon:
    """
    Run the epsilon greedy experiment for a range of epsilons
    """

    def __init__(
        self, epsilons, num_experiments=1, num_pulls=10000, verbose=True, drift=False
    ):
        self.epsilons = epsilons

        self.num_experiments = num_experiments
        self.num_pulls = num_pulls
        self.verbose = verbose
        self.drift = drift

    def run(self, plot=False):
        """
        Run the experiment for a range of epsilons
        """
        convergences = self.find_convergences()

        for eps, (average, best) in zip(self.epsilons, convergences):
            print(
                f"[Epsilon Greedy] "
                + f"eps = {eps:.2f} | "
                + f"experiments = {self.num_experiments} | "
                + f"pulls = {self.num_pulls} | "
                + f"avg_reward = {np.mean(average):.4f} | "
                f"best_reward_estimate = {np.mean(best):.4f}"
            )

            if plot:
                plt.plot(best, label=f"$\\epsilon$-Greedy: $\\epsilon$={eps:.2f}")

        if plot:
            self.show_plot("Epsilon Greedy")

    def find_convergences(self):
        """
        Find the convergences for a range of epsilons
        """
        estimated_convergences = []

        for eps in self.epsilons:
            algorithm = EpsilonGreedy(
                eps, self.num_experiments, self.num_pulls, self.drift
            )
            result = algorithm.run(self.verbose)
            estimated_convergences.append(result)

        return estimated_convergences

    def show_plot(self, title, limit=True):
        """
        Show the plot of the convergences
        """
        plt.title(f"Convergence of {title}")
        plt.xlabel("Pulls")
        plt.ylabel("Best Estimated Reward")
        plt.legend()

        if limit:
            plt.ylim(1.5, 2.5)

        plt.grid()
        plt.show()


def run_epsilon(epsilons, plot=False):
    """
    Run the epsilon greedy algorithm with the given epsilons
    """
    print("Question 2: Moving Sensors")
    experiment = ExperimentEpsilon(epsilons, num_experiments=10)
    experiment.run(plot)
    return experiment


"""
===============================================================================
Question 3: American Options - Franchises
===============================================================================
"""
"""
===============================================================================
Main Program
===============================================================================
"""


if __name__ == "__main__":
    # np.random.seed(122)  # seed for reproducibility

    parser = ArgumentParser()
    parser.add_argument(
        "question", help="Question 1, 2, or 3", type=int, choices=[1, 2, 3]
    )
    parser.add_argument("--plot", help="Plot the results", action="store_true")
    parser.add_argument(
        "--rejection",
        help="Use rejection (question 1 only)",
        choices=["half", "increasing"],
    )

    parser.add_argument(
        "--epsilons",
        help="Epsilon values to use (question 2 only)",
        nargs="+",
        type=float,
        default=[0.01, 0.05, 0.1, 0.4],
    )

    args = parser.parse_args()

    match vars(args):
        # Part 1: Finding a General Optimum
        case {"question": 1, "plot": should_plot, "rejection": rejection}:
            job_candidates(
                len_candidate=100,
                iterations=100000,
                plot=should_plot,
                rejection=rejection,
            )

        case {"question": 2, "plot": should_plot, "epsilons": epsilons}:
            run_epsilon(args.epsilons)

        case {"question": 3, "plot": should_plot, "rejection": _}:
            print("Running Question 3")

        # Invalid
        case _:
            parser.print_usage()
