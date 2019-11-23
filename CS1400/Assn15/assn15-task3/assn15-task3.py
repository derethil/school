from gronkyutil import convertCardToValue
from card import Card
from deck import Deck

DASHES = "-" * 25

def insertionSort(hand):
    for i in range(1, len(hand)):
        currCard = hand[i]
        j = i - 1

        while j >= 0 and hand[j].getValue() > currCard.getValue():
            hand[j + 1] = hand[j]
            j -= 1

        hand[j + 1] = currCard

def bubbleSort(hand):
    didSwap = True
    while didSwap:
        didSwap = False
        for i in range(len(hand) - 1):
            if hand[i].getId() > hand[i + 1].getId():
                hand[i], hand[i + 1] = hand[i + 1], hand[i]
                didSwap = True
        
def main():
    myDeck = Deck()
    myHand = [myDeck.draw() for num in range(31)]

    print("Welcome to the Gronky Deck Checker")

    menu = True
    prevSorted = False

    while menu:
        # Menu
        if not prevSorted:
            print(DASHES)
            print("New Hand")

        print(DASHES)
        print("1) Sort by value")
        print("2) Sort by ID")
        print("3) Find card")
        print("4) New hand")
        print("5) Exit")

        menuChoice = int(input("Make a selection: "))

        # Sort by value using insertion sort
        if menuChoice == 1:
            insertionSort(myHand)
            prevSorted = True
            print(DASHES)
            for card in myHand:
                print(card)

        # Sort by ID using bubble sort
        elif menuChoice == 2:
            bubbleSort(myHand)
            prevSorted = True
            print(DASHES)
            for card in myHand:
                print(card)

        # Find card
        elif menuChoice == 3:
            cardChoice = input("Please enter a card to search for (e.g. 10 of Rock Heads): ").lower().split()
            cardChoice.pop(1)
            print(cardChoice)
            tempCard = Card(convertCardToValue(int(cardChoice[0]), cardChoice[1], cardChoice[2]))
            print(tempCard)

            if tempCard in myHand:
                print("Your card is in the hand at position" + myHand.index(tempCard))
            else:
                print("Your card is not in the hand")



        # Get a new hand
        elif menuChoice == 4:
            myHand = [myDeck.draw() for num in range(31)]
            prevSorted = False

        elif menuChoice == 5:
            menu = False

main()