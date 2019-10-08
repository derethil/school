'''
Jaren Glenn
CS1400
Assn 7 - 2
See System analysis for description.

Software Development Lifecycle

System Analysis
This program will generate a pseudo random number and asking the user for a number as well.
Then it will output who won/loss or if it was a draw.
   0 - Scissors
   1 - Rock
   2 - Paper

System Design
1. Generate random integer between 0 and 2
2. Ask user for integer between 0 and 2
a. Translate computer and user into string for comparison
3. Define a function chooseWinner to choose the winner
3. Compare the choices and call chooseWinner as necessary
4. Print the who won or if it was a draw

Testing
Test #1:
   Input: 1
   Temporary computer choice: 2
   Output: Computer wins
   Passed
Test #2:
   Input: 0
   Temporary computer choice: 3
   Output: You win
   Passed
Test #3:
   Input: 1
   Temporary computer choice: 1
   Output: Draw
   Passed
'''
import random

# Get original choices
computer = random.randint(0,2)
user = input("Pick a number between 0 and 2 (0 = scissors, 1 = rock, 2 = paper): ").lower()

# Translate choices into words; more readable
choiceDict = {0: "scissors", 1: "rock", 2: "paper"}
computer = choiceDict[computer]
try:
   user = choiceDict[int(user)]
except:
   print("Input unrecognized. Please try again")

# Function to compare the two and print the winner
def chooseWinner(win, lose):
   if computer == win:
      print("The computer is %s. You are %s. You won!" % (computer, user))
   elif computer == lose:
      print("The computer is %s. You are %s. You lost." % (computer, user))

# First comparison block
if user == computer:
   print("The computer is %s. You are %s too. It is a draw." % (computer, user))
elif user == "scissors":
   chooseWinner("paper", "rock")
elif user == "rock":
   chooseWinner("scissors", "paper")
elif user == "paper":
   chooseWinner("rock", "scissors")