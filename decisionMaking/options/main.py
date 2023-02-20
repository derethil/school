from argparse import ArgumentParser

import numpy as np
import matplotlib.pyplot as plt
from fitter import Fitter
from scipy import stats

# Part 1 - Simple Option Classes


class European_Call_Payoff:
    def __init__(self, strike):
        self.strike = strike

    def get_payoff(self, stock_price):
        if stock_price > self.strike:
            return stock_price - self.strike
        else:
            return 0


class Simulation:
    def __init__(self, initial_price, drift, volatility, dt, T):
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

    def simulate_path(self):
        T = self.T
        while T - self.dt > 0:
            dWt = np.random.beta(14, 6) - 0.65
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

    def __init__(self, num_paths, initial_price, drift, volatility, dt, T) -> None:
        self.num_paths = num_paths
        self.price_paths: list[int] = []

        self.initial_price = initial_price
        self.drift = drift
        self.volatility = volatility
        self.dt = dt
        self.T = T

    def generate_paths(self):
        for i in range(self.num_paths):
            sim = Simulation(
                self.initial_price, self.drift, self.volatility, self.dt, self.T
            )
            price_path = sim.simulate_path()
            self.price_paths.append(price_path)

        return self.price_paths


# Part 1 - Simple Option Functions


def simple_option(num_paths, show_plot, outperform=None):
    model = Model(num_paths, 100, -0.01, 0.5, 1 / 365, 1)
    paths = model.generate_paths()

    if show_plot:
        for path in paths:
            plt.plot(path)
        plt.show()

    european_call = European_Call_Payoff(100, outperform=outperform)
    risk_free_rate = 0.01
    call_payoffs = [
        european_call.get_payoff(price_path[-1] / (1 + risk_free_rate))
        for price_path in paths
    ]

    print(f"Average final price: {np.mean([path[-1] for path in paths])}")
    print(f"Price: {np.mean(call_payoffs)}")


# Part 2 - Basket Option Functions


def basket_option(data_path):
    data = np.loadtxt(data_path, delimiter=",")
    fitter = Fitter(data)
    fitter.fit()

    print(fitter.summary(plot=True))

    best = fitter.get_best()
    print(best)


def main():
    parser = ArgumentParser()
    parser.add_argument(
        "part",
        help="Part of the assignment to run",
        choices=["simple_option", "fit_distribution", "debug"],
    )
    parser.add_argument(
        "-s",
        "--simulations",
        type=int,
        help="Number of paths to generate (only for simple option)",
        default=100,
    )
    parser.add_argument(
        "-p",
        "--plot",
        action="store_true",
        help="Show the plot (only for simple option)",
    )

    parser.add_argument(
        "-d",
        "--data",
        type=str,
        help="Path to the data file (only for fitting distributions)",
    )

    args = parser.parse_args()

    match vars(args):
        case {"part": "simple_option", "simulations": num_paths, "plot": show_plot}:
            simple_option(num_paths=num_paths, show_plot=show_plot)

        case {"part": "fit_distribution", "data": data_path}:
            if data_path is None:
                parser.print_usage()
                return

            basket_option(data_path)

        case {"part": "debug"}:
            breakpoint()

        case _:
            parser.print_usage()


if __name__ == "__main__":
    main()
