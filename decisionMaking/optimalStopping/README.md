# Code Guide

## Setup

The code expects numpy and matplotlib to be available.

## Part 1

- To run the experiment: `HW1_Glenn_Jaren.py part1`
- To run the tests: `HW1_Glenn_Jaren part1 --test`

## Part 2

- To run the experiment against uniform data: `HW1_Glenn_Jaren.py part1 uniform`
- To run the experiment against normal data: `HW1_Glenn_Jaren.py part1 normal`

The script also prints help if you enter bad input.

# Solutions

## Part 1

My code runs successfully on the tests; it finds that a good stopping point is index 9 on Scenario 1 and index 3 on Scenario 2. However, this does not follow the general 37% rule as it is only one candidate instead of thousands. When running the general optimum 37% rule, since the algorithm does not account for already having seen the largest value, no choice is found.

![Part 1 Result](./images/Part1.png)

When running the general experiment code over thousands of candidates, it results in the above graph. it matches up very well with what we would expect since it peaks around 37% and continues to decrease afterwards.

## Part 2

## Uniform

![Part 2 Uniform Result](./images/Part2Uniform.png)

## Normal

![Part 2 Normal Result](./images/Part2Normal.png)

While these two graphs aren't exactly the same, it makes sense that the threshold is significantly lower when we apply a penalty for how many times we explore the dataset. Uniform ended up at around 5% and Normal at around 8%.
