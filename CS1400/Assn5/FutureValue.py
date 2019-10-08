# Jaren Glenn
# CS1400 - 2
# Assn 5

# This program allows a user to enter their principal, annual rate, and x years of investment
# before printing out the accumulated value after x number of years

import math

# Ask the user for investment details
principal = float(input("Enter investment amount: "))
annualIntRate = float(input("Enter annual interest rate (4.25%): ").strip("%"))
totalYears = int(input("Enter number of years: "))

# Change interest rate from percent to a decimal
annualIntRate /= 100

# Convert to monthly inerest rate / number of months
monthlyIntRate = annualIntRate / 12
totalMonths = totalYears * 12

# Calculate accumulated value and print it out
accValue = principal * (1 + monthlyIntRate) ** totalMonths
roundedAccValue = round(accValue,2)
print("The accumulated value after %s years will be $%s" % (totalYears, roundedAccValue))

