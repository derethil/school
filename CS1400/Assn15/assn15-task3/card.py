class Card:
    __PAW = ["Rock", "Paper", "Scissor"]
    __COINS = ["Heads", "Tails"]   

    def __init__(self, val):
        self.__val = val
        
    def getId(self):
        return self.__val

    def getValue(self):
        return self.__val % 20 + 1

    def getPaw(self):
        return self.__PAW[self.__val // 40]

    def getCoin(self):
        return self.__COINS[self.__val % 2]

    def __str__(self):
        return "%s of %s %s" % (self.getValue(), self.getPaw(), self.getCoin())

    def __repr__(self):
        return self.__str__()
    
