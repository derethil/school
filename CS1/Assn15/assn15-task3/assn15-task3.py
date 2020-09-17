import gronkyutil
from card import Card
from deck import Deck

def main():
    # Setup
    myDeck = Deck()
    myDeck.shuffle()
    myHand = [myDeck.draw() for i in range(30)]

    DASHES = "-" * 30 #  Easy-to-access constant for number of dashes to print

    print("Welcome to the Gronky Deck Checker")
    print(DASHES)
    print("New Hand")

    menu = True
    while menu:
        # Menu
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
           
            print(DASHES)
            for card in myHand:
                print(card)

        # Sort by ID using bubble sort
        elif menuChoice == 2:
            bubbleSort(myHand)
           
            print(DASHES)
            for card in myHand:
                print(card)

        # Find card
        elif menuChoice == 3:
            searchValue = int(input("Enter a value to search for: "))

            print("1) Rock")
            print("2) Paper")
            print("3) Scissors")
            searchPaw = gronkyutil.paw[int(input("Choose a paw: ")) - 1]

            print("1) Heads")
            print("2) Tails")
            searchCoin = gronkyutil.coin[int(input("Choose a coin: ")) - 1]

            tempCard = Card(gronkyutil.convertCardToValue(searchValue, searchPaw, searchCoin))
            print(tempCard)
            
            bubbleSort(myHand)

            print(DASHES)
            if binarySearch(myHand, tempCard):
                print("Your card is in the deck")
            else:
                print("Your card is not in the deck")
            
        # Get a new hand
        elif menuChoice == 4:
            myHand = [myDeck.draw() for num in range(31)]
            print(DASHES)
            print("New Hand")


        elif menuChoice == 5:
            menu = False

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

def binarySearch(hand, card):
    low = 0
    high = len(hand) - 1

    while high >= low:
        mid = (high + low) // 2

        if card == hand[mid]:
            return True
        elif card.getId() < hand[mid].getId():
            high = mid - 1
        elif card.getId() > hand[mid].getId():
            low = mid + 1

    return False

main()