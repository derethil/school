import pdb

class Password():
    def __init__(self):
        self.__passText = ""
        self.__message = ""
        self.__valid = True

    def setPassword(self, passText):
        self.__resetPassword()
        self.__passText = passText

    def getErrorMessage(self):
        return self.__message

    def isValid(self):
        if len(self.__passText) < 8:
            self.__valid = False
            self.__message += " - Must contain at least 8 characters\n"

        if not self.__passText.isalnum():
            self.__valid = False
            self.__message += " - Must only contain numbers and letters\n"

        if sum(char.isdigit() for char in self.__passText) < 2:
            self.__valid = False
            self.__message += " - Must contain at least two numbers\n"

        if "password" in self.__passText:
            self.__valid = False
            self.__message += " - Must not contain password\n"

        if self.__passText.endswith("123"):
            self.__valid = False
            self.__message += " - Must not end with 123\n"

        # Removes the trailing \n for better readability
        self.__message = self.__message[:-2]

        return self.__valid

    def __resetPassword(self):
        self.__message = "Your password is not valid for the following reasons: \n"
        self.__valid = True

    