class Account:
    def __init__(self, idNum, balance, annualInterestRate):
        self.__id = idNum
        self.__balance = float(balance)
        self.__annualInterestRate = float(annualInterestRate)

    def getId(self):
        return self.__id

    def getBalance(self):
        return self.__balance

    def getAnnualInterestRate(self):
        return self.__annualInterestRate

    def setID(self, idNum):
        self.__id = idNum

    def __setBalance(self, balanceNum):
        self.__balance = balanceNum

    def setAnnualInterestRate(self, annualInterestRateNum):
        self.__annualInterestRate = annualInterestRateNum

    def getMonthlyInterestRate(self):
        return self.__annualInterestRate / 12

    def getMonthlyInterest(self):
        return self.__balance * (self.getMonthlyInterestRate() / 100) 

    def withdraw(self, amount):
        self.__setBalance(self.__balance - amount)

    def deposit(self, amount):
        self.__setBalance(self.__balance + amount)