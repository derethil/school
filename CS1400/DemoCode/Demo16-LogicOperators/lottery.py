import random

lottery = random.randint(0, 99)

guess = int(input("Enter a guess:"))

# Get individual digits from lottery and guess numbers
lotteryDigit1 = lottery // 10
lotteryDigit2 = lottery % 10
guessDigit1 = guess // 10
guessDigit2 = guess % 10

print("The lottery number is " + str(lottery))

if guess == lottery:
    print("Exact match. You win $1,000,000")
elif guessDigit1 == lotteryDigit2 and guessDigit2 == lotteryDigit1:
    print("Match all digits: $500,000")
elif guessDigit1 == lotteryDigit1 or guessDigit1 == lotteryDigit2 or guessDigit2 == lotteryDigit1 or guessDigit2 == lotteryDigit2:
    print("One Match: You win $100,000")
else:
    print("No matches. You lose $5,000,000")