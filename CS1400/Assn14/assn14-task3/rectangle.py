class Rectangle():
    def __init__(self, tur, position, height, width, color):
        self.__tur = tur
        self.__position = position
        self.__height = height
        self.__width = width
        self.__color = color

    def getPosition(self):
        return self.__position

    def getHeight(self):
        return self.__height

    def getWidth(self):
        return self.__width

    def getColor(self):
        return self.__color

    def draw(self):
         # Set up
        self.__tur.speed(10)
        self.__tur.penup()
        self.__tur.goto(self.__position[0], self.__position[1])

        # Draw the rectangle
        self.__tur.fillcolor(self.__color)
        self.__tur.begin_fill()
        self.__tur.setheading(90)
        self.__tur.forward(self.__height)
        self.__tur.right(90)
        self.__tur.forward(self.__width)
        self.__tur.right(90)
        self.__tur.forward(self.__height)
        self.__tur.right(90)
        self.__tur.forward(self.__width)
        self.__tur.end_fill()
