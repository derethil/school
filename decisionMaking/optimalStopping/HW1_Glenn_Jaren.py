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

    # not super useful for only 1 candidate but is the same as actual code for consistency
    for stop in range(1, len(candidate)):
        choice = np.argmax(candidate[stop:] > max(candidate[0:stop])) + stop
        if choice == optimal_choice:
            optimal_found[stop] += 1

    # Find what the stopping point would result in for comparison in output
    stop_point = np.argmax(optimal_found)
    largest_so_far = max(candidate[0:stop_point])
    next_largest = np.argmax(candidate[stop_point:] > largest_so_far) + stop_point

    return stop_point, next_largest


def thirty_seven_test(candidate):
    candidate = np.array(candidate)
    index = int((len(candidate) - 1) * 0.37)
    is_larger = candidate[index:] > max(candidate[0:index])
    return np.argmax(is_larger) + index if any(is_larger) else None


def show_test_results(title, scenario, stop_choice, result):
    print(title)
    print(f"{'Optimal Index: ':>19}{np.argmax(scenario)}")
    print(f"{'Optimal Result: ':>19}{scenario[np.argmax(scenario)]}")
    print(f"{'Stop Index: ':>19}{stop_choice}")
    print(f"{'Algorithm Result: ':>19}{scenario[result]}")
    print(f"{'37% Result: ':>19}{thirty_seven_test(scenario)}")


def run_tests():
    scenario1 = load_data("data/scenario1.csv")
    stop_choice, result = run_single_test(scenario1)
    show_test_results("Scenario 1", scenario1, stop_choice, result)

    print()

    scenario2 = load_data("data/scenario2.csv")
    stop_choice, result = run_single_test(scenario2)
    show_test_results("Scenario 2", scenario2, stop_choice, result)


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
