from scipy.special import expit
import math


class Neuron:
    def __init__(self, weights, bias):
        self.weights = weights
        self.bias = bias

    def __call__(self, inputs):
        weighted_inputs = [w * i for w, i in zip(self.weights, inputs)]
        return self.activation(sum(weighted_inputs) + self.bias)

    def activation(self, x):
        return expit(x)


l1n1 = Neuron([0.6, 0.5, -0.6], -0.4)
l1n2 = Neuron([-0.7, 0.4, 0.8], -0.5)
l2n1 = Neuron([1, 1], -0.5)

for x1 in [0, 1]:
    for x2 in [0, 1]:
        for x3 in [0, 1]:
            inputs = [x1, x2, x3]
            l1out = [l1n1(inputs), l1n2(inputs)]
            l2out = l2n1(l1out)
            print(
                "inputs: ",
                inputs,
                "outputs: ",
                [round(x, 2) for x in l1out],
                round(l2out, 3),
            )
