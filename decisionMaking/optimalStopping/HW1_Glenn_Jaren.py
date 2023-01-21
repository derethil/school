import sys
import numpy as np
import matplotlib.pyplot as plt

# Print progress to the console


def report_progress(experiment, iterations):
    if experiment % (iterations / 10) == 0:
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


def general_optimum(*, len_candidate, iterations):
    print("Part 1: Finding a General Optimum")
    optimal_found = np.zeros(len_candidate)

    for experiment in range(iterations):
        candidate = np.random.uniform(0, 99, size=len_candidate)
        optimal_choice = np.argmax(candidate)

        for stop in range(1, len_candidate):
            choice = np.argmax(candidate[stop:] > max(candidate[0:stop])) + stop
            if choice == optimal_choice:
                optimal_found[stop] += 1

        report_progress(experiment, iterations)

    print("\n", f"{np.argmax(optimal_found) / len_candidate:.2f}")
    plot_percent_found(optimal_found, iterations)


# Run the max benefit experiment


def max_benefit(*, len_candidate, iterations, distribution):
    print(f"Part 2: Max Benefit Stopping ({distribution.title()} Distribution)")
    optimal_found = np.zeros(len_candidate)

    for experiment in range(iterations):
        if distribution == "uniform":
            candidate = np.random.uniform(0, 99, size=len_candidate)
        elif distribution == "normal":
            candidate = np.random.normal(loc=50, scale=10, size=len_candidate)
            candidate = np.clip(candidate, 0, 99)

        optimal_choice = np.argmax(candidate)

        for stop in range(1, len_candidate):
            is_larger = candidate[stop:] - stop > max(candidate[0:stop])
            choice = np.argmax(is_larger) + stop
            if choice == optimal_choice:
                optimal_found[stop] += 1

        report_progress(experiment, iterations)

    print("\n", f"{np.argmax(optimal_found) / len_candidate:.2f}")
    plot_percent_found(optimal_found, iterations)


# Run tests against the given data


def load_data(filename):
    with open(filename) as f:
        return [int(line.strip()) for line in f.readlines()]


def run_single_test(candidate):
    candidate = np.array(candidate)
    optimal_found = np.zeros(len(candidate))
    optimal_choice = np.argmax(candidate)

    for stop in range(1, len(candidate)):
        choice = np.argmax(candidate[stop:] > max(candidate[0:stop])) + stop
        if choice == optimal_choice:
            optimal_found[stop] += 1

    return optimal_found


def run_tests():
    scenario1 = load_data("data/scenario1.csv")
    optimal_found = run_single_test(scenario1)

    print("Scenario 1")
    print(f"Optimal Choice: {np.argmax(scenario1)}")
    print(f"Choice: {np.argmax(optimal_found)}\n")

    scenario2 = load_data("data/scenario2.csv")
    optimal_found = run_single_test(scenario2)

    print("Scenario 2")
    print(f"Optimal Choice: {np.argmax(scenario2)}")
    print(f"Choice: {np.argmax(optimal_found)}")


# Main


if __name__ == "__main__":
    np.random.seed(122)  # seed for reproducibility
    match sys.argv:
        # Part 1: Finding a General Optimum
        case [_, "part1"]:
            general_optimum(len_candidate=100, iterations=2000)

        # Part 2: Max Benefit Stopping
        case [_, "part2", "uniform"]:
            max_benefit(len_candidate=100, iterations=2000, distribution="uniform")

        case [_, "part2", "normal"]:
            max_benefit(len_candidate=100, iterations=2000, distribution="normal")

        # Tests
        case [_, "part1", "--test"]:
            run_tests()

        case [_, "part2", "--test"]:
            print("Part 2: Max Benefit Stopping")
            print("This part has no tests.")

        # Invalid
        case _:
            print(f"Usage: {sys.argv[0]} [part1|part2] [uniform|normal|--test]")
