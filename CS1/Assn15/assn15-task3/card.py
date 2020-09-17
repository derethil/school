import gronkyutil

class Card:
    def __init__(self, _id):
        self.__id = _id

    def __str__(self):
        return "%s of %s %s" % (self.getValue(), self.getPaw(), self.getCoin())

    def __repr__(self):
        return self.__str__()

    def __eq__(self, other):
        return True if self.getId() == other.getId() else False
        
    def getId(self):
        return self.__id

    def getValue(self):
        tempValue = self.__id
        while tempValue >= 40:
            tempValue -= 40
        if tempValue % 2 == 0:
            return (tempValue + 2) // 2
        else:
            return (tempValue + 1) // 2

    def getPaw(self):
        return self.gronkyutil.paw[self.__id // 40]

    def getCoin(self):
        return self.gronkyutil.coin[self.__id % 2]
    
