import random
import sys

import matplotlib.pyplot as plt
import numpy as np

# Part 1


def general_optimum_iteration(optimal_found, candidate, len_candidate):
    optimal_answer = max(candidate)

    for stopping_point in range(1, len_candidate):
        comparison_value = max(candidate[:stopping_point])
        for i in range(0, len(candidate[stopping_point:-1])):
            if candidate[i] > comparison_value:
                if candidate[i] == optimal_answer:
                    optimal_found[stopping_point] += 1
                break


def find_general_optimum(*, len_candidate, iterations):
    optimal_found = [0] * len_candidate

    for _ in range(iterations):
        candidate = [random.uniform(0, 99) for _ in range(len_candidate)]
        general_optimum_iteration(optimal_found, candidate, len_candidate)

    avg = avg_stopping_point(optimal_found)
    percent = f"{(avg / len_candidate) * 100:.2f}%"

    return (optimal_found, percent)


# Part 2


def iterate_part2(optimal_found, candidate, len_candidate):
    optimal_answer = max(candidate)

    for stopping_point in range(1, len_candidate):
        comparison_value = max(candidate[:stopping_point]) - stopping_point
        for i in range(0, len(candidate[stopping_point:-1])):
            if (candidate[i] - stopping_point - i) > comparison_value:
                if candidate[i] == optimal_answer:
                    optimal_found[stopping_point] += 1
                break


# Helpers


def avg_stopping_point(optimal_found):
    return sum([i * v for i, v in enumerate(optimal_found)]) / sum(optimal_found)


def read_data(filename):
    with open(filename) as f:
        return [int(line.strip()) for line in f.readlines()]


# Main


def run_tests():
    scenario1 = read_data("data/scenario1.csv")

    optimal_found = [0] * len(scenario1)
    general_optimum_iteration(optimal_found, scenario1, len(scenario1))

    print("Scenario 1")
    print(f"Max: {max(scenario1)}")
    print(f"Found Optimal: {avg_stopping_point(optimal_found)}\n")

    scenario2 = read_data("data/scenario2.csv")

    optimal_found = [0] * len(scenario2)
    iterate_part2(optimal_found, scenario2, len(scenario2))

    print("Scenario 2")
    print(f"Max: {max(scenario2)}")
    print(f"Optimal: {avg_stopping_point(optimal_found)}")


if __name__ == "__main__":
    match sys.argv:
        case [_, "part1", "--test", *rest]:
            run_tests()

        case [_, "part1", *rest]:
            found, percent = find_general_optimum(len_candidate=1000, iterations=2500)
            print(f"Optimal: {percent}")

            plt.plot(found)
            plt.xlabel("Stopping Point")
            plt.ylabel("Optimal Solutions Found")
            plt.show()

        case _:
            print("Usage: python main.py [part1|part2] [--test]")
