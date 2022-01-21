import turtle

class Chessboard():
    def __init__(self, turtle, startX, startY, height=250, width=250):
        self.__startX = startX
        self.__startY = startY
        self.__height = height
        self.__width = width
        self.__tur = turtle

    def draw(self):
        # Turtle setup
        self.__tur.speed(10)
        self.__tur.penup()
        self.__tur.goto(self.__startX, self.__startY)

        self.__drawAllRectangles()

    def __drawRectangle(self, rectHeight, rectWidth):
        self.__tur.pendown()
        self.__tur.setheading(90)
        self.__tur.forward(rectHeight)
        self.__tur.setheading(0)
        self.__tur.forward(rectWidth)
        self.__tur.setheading(270)
        self.__tur.forward(rectHeight)
        self.__tur.setheading(180)
        self.__tur.forward(rectWidth)
        self.__tur.penup()

    def __drawRow(self, rectCount):
        # Get the height and width of each individual rectangle
        rectHeight = self.__height / rectCount
        rectWidth = self.__width / rectCount

        # Draw one row of rectangles
        for rect in range(rectCount // 2):
            self.__tur.begin_fill()
            self.__drawRectangle(rectHeight, rectWidth)
            self.__tur.end_fill()
            self.__tur.goto(self.__tur.xcor() + 2 * (rectWidth), self.__tur.ycor())

    def __drawAllRectangles(self):
        # Draw the border for the entire chessboard
        self.__tur.width(3)
        self.__drawRectangle(self.__height, self.__width)  
        self.__tur.width(.5)

        NUM_OF_RECTS = 8  # Number of rectangles on one side of the chessboard
        START_X = self.__tur.xcor()

        # This simply calls the drawRow function as many times as needed to draw the alternating rectangles.
        # After each call it resets the position to either the far left of the board or one square over, depending on alternation.
        for doubleRow in range(NUM_OF_RECTS // 2):
            self.__drawRow(NUM_OF_RECTS)
            self.__tur.goto(START_X + self.__width / NUM_OF_RECTS, self.__tur.ycor() + (self.__height / NUM_OF_RECTS))
            self.__drawRow(NUM_OF_RECTS)
            self.__tur.goto(START_X, self.__tur.ycor() + (self.__height / NUM_OF_RECTS))
