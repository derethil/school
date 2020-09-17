'''
Jaren Glenn
CS1400 - 2
Assn 14 - Task 1
'''

class Cuboid():
    def __init__(self, length, width, height):
        self.__length = length
        self.__width = width
        self.__height = height

    def getVolume(self):
        return self.__length * self.__width * self.__height

    def getSurfaceArea(self):
        return 2 * (self.__length * self.__width + self.__length * self.__height + self.__height * self.__width)

    def __add__(self, other):
        sideLength = (self.getVolume() + other.getVolume()) ** (1/3)
        return Cuboid(sideLength, sideLength, sideLength)

    def __sub__(self, other):
        sideLength = (self.getVolume() - other.getVolume()) ** (1/3)

        # If statement makes sure the dimensions can't be negative, and returns a cuboid with a volume 0 if so
        if type(sideLength) == float or type(sideLength) == int:
            return Cuboid(sideLength, sideLength, sideLength)
        else:
            return Cuboid(0,0,0)

    def __gt__(self, other):
        if self.getVolume() > other.getVolume():
            return True
        else:
            return False

    def __lt__(self, other):
        if self.getVolume() < other.getVolume():
            return True
        else:
            return False

    def __eq__(self, other):
        if self.getVolume() == other.getVolume():
            return True
        else:
            return False

    def __len__(self):
        return int(self.getSurfaceArea())

    # Create a nice looking string to print when calling str()
    def __str__(self):
        infoStr = format("Length:", "<15s") + format(str(round(self.__length, 3)), ">7s") + "\n"
        infoStr += format("Width:", "<15s") + format(str(round(self.__width, 3)), ">7s") + "\n"
        infoStr += format("Height:", "<15s") + format(str(round(self.__height, 3)), ">7s") + "\n"
        infoStr += format("Volume:", "<15s") + format(str(round(self.getVolume(), 3)), ">7s") + "\n"
        infoStr += format("Surface Area:", "<15s") + format(str(round(self.getSurfaceArea(), 3)), ">7s")

        return infoStr
