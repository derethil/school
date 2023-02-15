import numpy as np
import matplotlib.pyplot as plt
from argparse import ArgumentParser


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


def simple_option(num_paths, show_plot):
    model = Model(num_paths, 100, -0.01, 0.5, 1 / 365, 1)
    paths = model.generate_paths()

    if show_plot:
        for path in paths:
            plt.plot(path)
        plt.show()

    european_call = European_Call_Payoff(100)
    call_payoffs = [european_call.get_payoff(price_path[-1]) for price_path in paths]

    print(f"Average final price: {np.mean([path[-1] for path in paths])}")
    print(f"Average payoff: {np.mean(call_payoffs)}")


def basket_option():
    pass


def main():
    parser = ArgumentParser()
    parser.add_argument("part", type=int, help="Part of the assignment to run")
    parser.add_argument(
        "-s", "--simulations", type=int, help="Number of paths to generate", default=100
    )
    parser.add_argument(
        "-p", "--plot", action="store_true", help="Show the plot (only for part 1)"
    )

    args = parser.parse_args()

    match vars(args):
        case {"part": 1, "simulations": num_paths, "plot": show_plot}:
            simple_option(num_paths=num_paths, show_plot=show_plot)

        case {"part": 2}:
            basket_option()

        case _:
            parser.print_usage()


if __name__ == "__main__":
    main()
