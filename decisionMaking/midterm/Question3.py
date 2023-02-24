from argparse import ArgumentParser

import numpy as np
import matplotlib.pyplot as plt
from fitter import Fitter
from scipy import stats

# Part 1 - Simple Option Classes


class EuropeanCallPayoff:
    def __init__(self, strike):
        self.strike = strike

    def get_payoff(self, stock_price):
        if stock_price > self.strike:
            return stock_price - self.strike
        else:
            return 0


class Simulation:
    def __init__(self, initial_price, drift, volatility, dt, T, distribution):
        # Model Parameters
        self.current_price = initial_price
        self.initial_price = initial_price
        self.drift = drift
        self.volatility = volatility
        # Time Parameters
        self.dt = dt
        self.T = T
        # Price Path
        self.prices = []
        # Distribution
        self.d = distribution

    def simulate_path(self):
        T = self.T
        while T - self.dt > 0:
            dWt = self.d["func"](*self.d["args"]) + self.d["shift"]
            dYt = self.drift * self.dt + self.volatility * dWt
            self.current_price += dYt
            self.prices.append(self.current_price)
            T -= self.dt

        return self.prices


class Model:
    initial_price = 100
    drift = -0.01
    volatility = 0.5

    dt = (T := 1) / 365

    def __init__(
        self, num_paths, initial_price, drift, volatility, dt, T, dist
    ) -> None:
        self.num_paths = num_paths
        self.price_paths: list[int] = []

        self.initial_price = initial_price
        self.drift = drift
        self.volatility = volatility
        self.dt = dt
        self.T = T
        self.dist = dist

    def generate_paths(self):
        for _ in range(self.num_paths):
            sim = Simulation(
                self.initial_price,
                self.drift,
                self.volatility,
                self.dt,
                self.T,
                self.dist,
            )
            price_path = sim.simulate_path()
            self.price_paths.append(price_path)

        return self.price_paths


dist = {
    "func": np.random.beta,
    "args": (14, 6),
    "shift": -0.65,
}


def option_price(num_paths, show_plot, strike, dist=dist):
    model = Model(num_paths, 100, -0.01, 0.5, 1 / 365, 1, dist)
    paths = model.generate_paths()

    if show_plot:
        for path in paths:
            plt.plot(path)
        plt.show()

    european_call = EuropeanCallPayoff(strike)
    risk_free_rate = 0.01
    call_payoffs = [
        european_call.get_payoff(price_path[-1] / (1 + risk_free_rate))
        for price_path in paths
    ]

    print(f"  Option Price: {np.mean(call_payoffs):.2f}")
    print(f"  Option Block Price (100): {np.mean(call_payoffs) * 100:.2f}")


def fit_distribution(data, title):
    print(
        f"====================\nFitting distribution to {title}:\n===================="
    )

    fitter = Fitter(data)
    fitter.fit()

    print(fitter.summary(plot=False))
    print(f"Best distribution: {fitter.get_best()}")


def main():
    parser = ArgumentParser()
    parser.add_argument(
        "part",
        help="Part of the assignment to run",
        choices=["p1", "p2"],
    )

    parser.add_argument(
        "-p",
        "--plot",
        action="store_true",
        help="Show the plot",
    )

    parser.add_argument(
        "--no-fit",
        action="store_true",
        help="Don't fit the distribution",
    )

    args = parser.parse_args()

    match vars(args):
        case {"part": "p1", "plot": show_plot}:
            print("====================\nSIMULATING STOCK PRICES\n====================")
            option_price(
                num_paths=5000,
                show_plot=show_plot,
                strike=100,
            )

        case {"part": "p2", "plot": show_plot, "no_fit": no_fit}:
            stock1 = np.loadtxt("data/stock1.csv", delimiter=",")
            stock2 = np.loadtxt("data/stock2.csv", delimiter=",")

            if not no_fit:
                print("====================\nFITTING DISTRIBUTIONS")
                fit_distribution(stock1, "Stock 1")
                fit_distribution(stock2, "Stock 2")

            print("====================\nOUTPERFORMING AVERAGE\n====================")
            avg_strike1 = np.mean(stock1)
            avg_strike2 = np.mean(stock2)

            print(f"Stock 1 (strike={avg_strike1:.2f}):")
            option_price(
                num_paths=5000,
                show_plot=False,
                strike=avg_strike1,
            )
            print(f"Stock 2 (strike={avg_strike2:.2f}):")
            option_price(
                num_paths=5000,
                show_plot=False,
                strike=avg_strike2,
            )

            print("====================\nOUTPERFORMING MAX\n====================")
            strike = np.min([np.max(stock1), np.max(stock2)])
            print(f"Strike: {strike:.2f}")
            option_price(
                num_paths=5000,
                show_plot=show_plot,
                strike=strike,
            )

        case {"part": "plot"}:
            # github copilot: draw 10 samples from stats.f

            print(stats.f.rvs(4688.62, 36772.73, size=10, loc=-130.29, scale=230.1985))
            print(stats.alpha.rvs(25.8826, loc=-54.3223, scale=4061.97, size=10))

        case _:
            parser.print_usage()


if __name__ == "__main__":
    main()
