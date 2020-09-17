'''
Jaren Glenn
CS1400
Assn 8 - 1

Software Development Lifecycle

Required Specification
    Find all the perfect numbers between 1 and 10000 using nested for loops. Display them to the user.
    A perfect number is defined as a number whose positive integers (excluding itself) add up to itself.

System Analysis
    The program will loop through each number between 1 and 10000 and determine if it is a perfect number 
    using a inner for loop to add its divisors.

System Design
    1. For loop to check every number under 10,000
    2. Create a list of its positive divisors using %
    3. Add its divisors together 
    4. Check the value against the original number
    5. If it is a perfect number, add it to a list of perfect numbers

Testing
    Output:
        [6, 28, 496, 8120]
        <x> iterations of inner loop (line 42)
        Passed
'''
iterationCount = 0
perfectNumberList = []

from math import sqrt

# Main loop to iterate through every value
for currentNum in range(1,10000):
    divisorList = []
    
    # Uses sqrt to massively improve efficiency. The currentNum can be divided by every divisor below its square root to get 
    # its matching divisor, without increasing the iteration count.

    for possibleDivisor in range(1, int(sqrt(currentNum) + 1)):
        iterationCount += 1

        if currentNum % possibleDivisor == 0:
            divisorList.append(possibleDivisor)
            if currentNum // possibleDivisor != currentNum: # Disallows for the currentNum to be added to its own divisor list
                divisorList.append(currentNum // possibleDivisor)

    # Check value against currentNum 
    if sum(divisorList) == currentNum and currentNum != 1:
        perfectNumberList.append(currentNum)

print(perfectNumberList)
print(str(iterationCount) + " iterations of inner loop")

