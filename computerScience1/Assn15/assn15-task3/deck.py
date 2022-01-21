from random import shuffle

from card import Card

class Deck:
    def __init__(self):
        self.__deck = [Card(num) for num in range(120)]

    def __str__(self):
        return str([str(card) for card in self.__deck])

    def shuffle(self):
        shuffle(self.__deck)

    def draw(self):
        return self.__deck.pop()
             