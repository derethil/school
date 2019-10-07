'''
Jaren Glenn
CS1400
Assn 8 - 1

See system analysis for description.

Software Development Lifecycle

System Analysis
This program will find the four perfect numbers under 10,000 and print them out. 
A perfect number is defined as a number whose value is the same as all its positive 
divisors, not including itself.

System Design
1. For loop to check every number under 10,000
2. Create a list of its positive divisors using %
3. Add its divisors together 
4. Check the value against the original number
5. If it is a perfect number, add it to a list of perfect numbers
'''
iterationCount = 0
perfectNumberList = []

# Main loop to iterate through every value
for currentNum in range(100):
    divisorSum = 0

    # Check divisors and add them together
    for divisor in range(1, (currentNum // 2) + 1):
        iterationCount += 1
        if currentNum % divisor == 0:
            divisorSum += divisor

    # Check value against currentNum
    if divisorSum == currentNum:
        perfectNumberList.append(currentNum)

print(perfectNumberList)
print(str(iterationCount) + " iterations of inner loop")

