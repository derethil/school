import turtle

# Draw one chessboard square
def drawSquare(color, height, width):
    pass 

# Main draw chessboard loop
def drawChessboard(coordX, coordY, height=250, width=250):
    t = turtle.Turtle()

    t.penup()
    t.goto(coordX, coordY)

    for row in range(9):
        print("lol")