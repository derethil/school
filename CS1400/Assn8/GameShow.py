'''
Jaren Glenn
CS1400
Assn 8-2

Software Development Lifecycle

Requirement Specification
    This program will simulate a game show 100,000 times. The game works as following:
    Given three doors, one hides a car and the other two hide goats. The player chooses a door (ex. #1) and then one of the 
    other doors' (that holds a goat, ex. #2) will be shown to the player. The player then chooses whether or not to switch 
    their choice to the remaining door (ex. #3). 
    The program will determine whether or not the player has a better chance of winning the car if they switch their choice.

System Analysis
    Without switching, it is a simple comparison between three random integers. If switching is selected, then the remaining guesses
    start as a list of doors. THe users's guess will be removed from this and, since the host will never reveal the winning door,
    the revealed door is equal to either the non-winning door or, if the user picked correctly the first time, a random door. Then the 
    revealed door will be removed from the remaining guesses list.

System Design
    1. Ask the user if they want to simulate switching their choice for each of the 100,000 iterations
    2. Create a main loop to run 100,000 times
    3. Generate prize door
    4. Generate player choice
    5. If user input says to not switch, continue, else
        a. Reveal a random wrong remaining door and switch the guess to the remaining door that has not been guessed or revealed
    6. If the user won, add it to a total won count
    7. Print the total won count

Testing
    Input:
        No
    Output:
        You won the game X times. (Around 1/3)
    Passed
    
    Input:
        Yes
    Output:
        You won the game X times. (Around 2/3)
    Failed - my logic was wrong and had to rewrite the switching part in a new way
    
'''
from random import randint

totalWins = 0
switchChoice = input("Would you like to switch your choice for each of the 100,000 iterations? (y/n) ").lower()

# Main loop
for iterCount in range(100001):
    # Generate initial guess and winning door
    prize = randint(1,3) # 2
    guess = randint(1,3) # 3

    # Remove guess from remaining doors
    remainingGuesses = [1,2,3]
    remainingGuesses.remove(guess)

    # Reveal a door
    if remainingGuesses[0] == prize:
        del remainingGuesses[1]
    elif remainingGuesses[1] == prize:
        del remainingGuesses[0]
    else:
        del remainingGuesses[randint(0, len(remainingGuesses)) - 1]

    # Switches to the door depending on input
    if switchChoice == "y":
        guess = remainingGuesses[0]

    # Increment totalWins
    if guess == prize:
        totalWins += 1

print("You won the game %s times." % totalWins)

