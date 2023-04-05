# Part 1

Consider the revenue numbers of indie-developed video games. Different genres of indie games have different revenue success probabilities, and most indie games do not make a profit at all. I can hazard a guess and say that since RPGs tend to be the most successful genre, that will significantly increase the chances of gaining more than $50,000 in revenue.

Considering the fact that I have exact values and probabilities (from 2020 data), this would be a great application of Bayes' theorem. I could take the probability that a game is an RPG, and the probability that a game made over $50,000, and the probability that a game is an RPG given it made over $50,000 and see how that would inform my hypothesis.

# Part 2

We're given the following information:

$$P(\text{Vampires}) = 0.05$$
$$P(\text{Sparkle | Vampires}) = 0.7$$
$$P(\text{Sparkle | No Vampires}) = 0.03$$

From this we can find that

$$P(\text{No Vampires}) = 1 - P(\text{Vampires}) = 0.95$$

and then from the law of total probability

$$
\begin{align*}
P(\text{Sparkles}) &= P(\text{Sparkle | Vampires}) \times P(\text{Vampires}) \\
                   &+ P(\text{Sparkle | No Vampires}) \times P(\text{No Vampires}) \\
                   &= 0.7 \times 0.05 + 0.03 \times 0.95 \\
                   &= 0.0635
\end{align*}
$$

With this we can use Bayes' theorem:

$$
\begin{align*}
P(\text{Vampires | Sparkle}) &= \frac{P(\text{Sparkle | Vampires}) \times P(\text{Vampires})}{P(\text{Sparkle})} \\
&= \frac{0.7 \times0.05}{0.0635} \\
&=  0.5512

\end{align*}
$$

So, given Edward sparkled like a diamond, there is a probability of .5512 that vampires exist.

# Part 3

Converting the information from the 20 surveyed commuters gives the prior

| $\pi$    | 0.15 | 0.25 | 0.50 | 0.75 | 0.85 |
| -------- | ---- | ---- | ---- | ---- | ---- |
| $f(\pi)$ | 0.15 | 0.15 | 0.40 | 0.15 | 0.15 |

and then we can calculate the likelihood using a binomial model of Bin(13, $\pi$).

| $\pi$             | 0.15  | 0.25  | 0.50  | 0.75 | 0.85 |
| ----------------- | ----- | ----- | ----- | ---- | ---- |
| $L(\pi\vert y=3)$ | 0.190 | 0.251 | 0.035 | ~0   | ~0   |

Additionally, we can find the normalizing constant via

$$
f(y=3) = 0.19 \times 0.15 + 0.251 \times 0.15 + 0.035 \times 0.4 + 0.015 \times 0 + 0.015 \times 0 = 0.0802
$$

And then we can calculate the posterior:

$$f(\pi=0.15\vert y=3) = \frac{0.15 \times 0.19}{0.0802} = 0.3563$$
$$f(\pi=0.25\vert y=3) = \frac{0.15 \times 0.252}{0.0802} = 0.4725$$
$$f(\pi=0.5\vert y=3) = \frac{0.4 \times 0.035}{0.0802} = 0.175$$
$$f(\pi=0.75\vert y=3) = \frac{0.15 \times 0}{0.0802} = 0$$
$$f(\pi=0.85\vert y=3) = \frac{0.15 \times 0}{0.0802} = 0$$

It's pretty clear from the posterior that it's much more likely that the probability the bus is late is smaller than what the average commuter thought. There's almost no chance it's above 0.75 and even the chance that the probability is 0.5 is comparatively pretty small.

I think it's pretty cool that with only 13 days of data Li Qiang was able to reduce the uncertainty of the bus being late by that much.
