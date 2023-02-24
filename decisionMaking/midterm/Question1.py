import sys
import matplotlib.pyplot as plt
from argparse import ArgumentParser

import numpy as np

# Print progress to the console


def report_progress(experiment, iterations):
    if experiment % (iterations / 100) == 0:
        percent = experiment / iterations * 100
        print(f"Running... {percent:.0f}%", end="\r", flush=True)


# Plot the percent of optimal solutions found


def plot_percent_found(optimal_found, iterations):
    percent_found = optimal_found / iterations

    plt.plot(percent_found)
    plt.xlabel("Stopping Point")
    plt.ylabel("Percent Optimal Solutions Found")
    plt.show()


# Run the general optimum experiment


def latest_candidates(candidate, choice, *, rejection):
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
    if rejection:
        print(f"Question 1: Job Candidates (with {rejection} rejection)")
    else:
        print("Question 1: Job Candidates (no rejection)")

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


# Main


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

        case {"question": 2, "plot": should_plot, "rejection": _}:
            print("Running Question 2")

        case {"question": 3, "plot": should_plot, "rejection": _}:
            print("Running Question 3")

        # Invalid
        case _:
            parser.print_usage()
