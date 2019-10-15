import turtle, pdb


# Draw a rectangle with specified height and width
def drawRectangle(t, recHeight, recWidth):
    t.pendown()
    t.setheading(90)
    t.forward(recHeight)
    t.setheading(0)
    t.forward(recWidth)
    t.setheading(270)
    t.forward(recHeight)
    t.setheading(180)
    t.forward(recWidth)
    t.penup()

# Draw a row of rectangles
def drawRow(t, totalHeight, totalWidth, rectCount):
    rectHeight = totalHeight / rectCount
    rectWidth = totalWidth / rectCount

    for rect in range(rectCount // 2):
        t.begin_fill()
        drawRectangle(t, rectHeight, rectWidth)
        t.end_fill()
        t.goto(t.xcor() + 2 * (rectWidth), t.ycor())
            
# Draw all chessboard squares function
def drawAllRectangles(t, totalHeight, totalWidth):
    turtle.width(10)
    drawRectangle(t, totalHeight, totalWidth) # Draw the border for the entire chessboard
    turtle.width(5)

    NUM_OF_RECTS = 8 # Number of rectangles on one side of the chessboard
    START_X = t.xcor()
    START_Y = t.ycor()

    # This simply calls the drawRow function as many times as needed to draw the alternating rectangles.
    # After each call it resets the position to either the far left of the board or one square over, depending on alternation.
    for doubleRow in range(NUM_OF_RECTS // 2):
        drawRow(t, totalHeight, totalWidth, NUM_OF_RECTS)
        t.goto(START_X + totalWidth / NUM_OF_RECTS, t.ycor() + (totalHeight / NUM_OF_RECTS))
        drawRow(t, totalHeight, totalWidth, NUM_OF_RECTS)
        t.goto(START_X, t.ycor() + (totalHeight / NUM_OF_RECTS))

        
    
# Main draw chessboard loop
def drawChessboard(coordX, coordY, width=250, height=250):
    t = turtle.Turtle()
    t.speed(10)
    t.penup()
    t.goto(coordX, coordY)

    drawAllRectangles(t, height, width)

    turtle.done()