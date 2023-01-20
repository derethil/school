import random
import matplotlib.pyplot as plt

def plot_stopping_rule(optimal_solution_found):
    plt.plot(optimal_solution_found)
    plt.xlabel('Stopping Point')
    plt.ylabel('Optimal Solutions Found')
    plt.show()

def find_stopping_rule(*, num_candidates, iterations):
    optimal_solutions_found = [0] * num_candidates

    for _ in range(iterations):
        candidates = [random.uniform(0, 99) for _ in range(num_candidates)]
        optimal_answer = max(candidates)
        for stopping_point in range(1, num_candidates):

            comparison_value = max(candidates[:stopping_point]) # this is the optimal stopping rule

            for i in range(0, len(candidates[stopping_point:-1])):
                # if (candidates[i] - stopping_point - i) > comparison_value:
                if (candidates[i] > comparison_value):
                    if candidates[i] == optimal_answer:
                        optimal_solutions_found[stopping_point] += 1
                    break


    avg = sum([idx * found for idx, found in enumerate(optimal_solutions_found)]) / len(optimal_solutions_found)

    print(f"{avg / iterations * 99:.2f}%")

    return optimal_solutions_found


found = find_stopping_rule(num_candidates=100, iterations=5000)
plot_stopping_rule(found)
