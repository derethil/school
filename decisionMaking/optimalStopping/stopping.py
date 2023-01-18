import random
import sys

def find_max_after(nums, i):
    max_in_first_i = max(nums[:i]) if i > 0 else nums[i]

    for num in nums[i:]:
        if num > max_in_first_i:
            return num

    return max_in_first_i


def find_optimal(nums, actual_max):
    for i in range(1, len(nums)):
        choice = find_max_after(nums, i)
        if choice == actual_max:
            return i


def general_optimum(iterations=5000, n=1000, max_num=100):
    optimal_points = []
    for iteration in range(iterations):
        nums = [int(random.uniform(0, max_num)) for _ in range(n)]
        optimal = find_optimal(nums, max(nums))
        optimal_points.append(optimal)

        if (iteration % (iterations / 50)) == 0: print(".", end="", flush=True)

    avg_optimal_point = sum(optimal_points) / len(optimal_points)
    optimal_point_pct = avg_optimal_point / max_num * 100

    return f"\nOptimal: {optimal_point_pct:.4f}%"

def run_tests():
    pass

if __name__ == "__main__":
    match sys.argv:
        case [_, "run", "main", *rest]:
            print(general_optimum(n=1000, iterations=10000))
        case [_, "run", "tests", *rest]:
            run_tests()