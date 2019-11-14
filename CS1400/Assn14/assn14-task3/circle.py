class Circle():
    def __init__(self, tur, position, radius, color):
        self.__tur = tur
        self.__position = position
        self.__radius = radius
        self.__color = color

    def getPosition(self):
        return self.__position

    def getRadius(self):
        return self.__radius

    def getColor(self):
        return self.__color

    def draw(self):
        # Set up
        self.__tur.speed(10)
        self.__tur.penup()
        self.__tur.goto(self.__position[0], self.__position[1] - self.__radius)

        # Draw the circle 
        self.__tur.fillcolor(self.__color)
        self.__tur.begin_fill()
        self.__tur.circle(self.__radius)
        self.__tur.end_fill()
