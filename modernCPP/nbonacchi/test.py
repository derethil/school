from functools import reduce

def nbonacchi(series, n):
    if n < series + 1:
        return 1
    else:
        # total = 0
        return reduce(nbonacchi, range(n - series, n))

        # breakpoint()

        # for i in range(n - series, n):
        #     total += nbonacchi(series, i)

        # return total

breakpoint()