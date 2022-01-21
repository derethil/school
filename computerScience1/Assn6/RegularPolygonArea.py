# Jaren Glenn
# CS1400

# Software Development Lifecycle

# System analysis
# This program needs to ask for a side length and the number of sides on a
# regular polynomial, compute the area, and then print the area out.

# System Design
# 1. Get input for number of sides
# 2. Get input for length of the side
# 3. Calculate area using the equation (number of sides * (length of sides) ** 2) / (4 * tan(pi / number of sides))
# 4. Print area

# Testing
# Test 1:
#   Input:
#     Number of sides: 5
#     Side length: 6.5
#   Output:
#     Area: 73.69
#   Status:
#     Failed - Had to convert inputs from strings to floats
# Test 2:
#   Input:
#     Number of sides: 10
#     Side length: 6
#   Output:
#     Area: 276.99
#   Status:
#     Passed

import math

numOfSides = int(input("Input # of sides: "))
lenOfSides = float(input("Input length of sides: "))

area = round((numOfSides * lenOfSides ** 2) / (4 * math.tan(math.pi / numOfSides)), 2)

print("The area of your polygon is %s" % area)


