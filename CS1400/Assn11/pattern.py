'''
Jaren Glenn
CS1400 - 2
Assn 11
'''
import turtle
from random import randint

tr = turtle.Turtle()

# Erase all patterns and start over
def reset():
    tr.reset()
    setup()

def setup():
    # turtle.tracer(0)
    turtle.setup(width=1000, height=800)
    tr.speed(0)
    tr.hideturtle()
    tr.penup()

# Draws rectangle pattern
def drawRectanglePattern(centerX, centerY, offset, height, width, count, rotation):
    centerPos = centerX, centerY
    tr.goto(centerPos)

    for i in range(1, count + 1):
        tr.forward(offset)
        tr.right(rotation) # Orients the rectangles to be properly drawn
        drawRectangle(height, width, setRandomColor())

        # This completely resets the turtle back to the center of the shape. 
        # It's then rotated x number of times that it's already done it, so it doesn't overlap.
        tr.goto(centerPos)
        tr.setheading(0)
        tr.left((360 / count) * i)

# Draw a single rectangle in a color
def drawRectangle(height, width, color):
    tr.pencolor(color)
    tr.pendown()
    tr.forward(height)
    tr.left(90)
    tr.forward(width)
    tr.left(90)
    tr.forward(height)
    tr.left(90)
    tr.forward(width)
    tr.penup()

# Draw circle pattern
def drawCirclePattern(centerX, centerY, offset, radius, count):
    centerPos = centerX, centerY
    tr.goto(centerPos)

    for i in range(1, count + 1):
        tr.forward(offset)
        tr.right(90)  # This makes the closest point <offset> units away from centerPos
        tr.pencolor(setRandomColor())
        
        tr.pendown()
        tr.circle(radius)
        tr.penup()
        
        # See drawRectanglePattern
        tr.goto(centerPos)
        tr.setheading(0)
        tr.left((360 / count) * i)

# Draw random number of shapes
def drawSuperPattern(count=5):
    for i in range(count + 1):
        centerX, centerY = randint(-400,400), randint(-300,300)
        count = randint(1,50)
        offset = randint(-75,125)
        

        patternType = randint(0,1)

        # RectanglePattern
        if patternType:  
            height = randint(1,200)
            width = randint(1,150)
            rotation = randint(-360,360)
            drawRectanglePattern(centerX, centerY, offset, height, width, count, rotation)

        # CirclePattern
        else:  
            radius = randint(1,150)
            drawCirclePattern(centerX, centerY, offset, radius, count)


def setRandomColor():
    return ["red", "green", "blue"][randint(0,2)]

# Makes the turtle stay open after user finish
def done():
    turtle.done()
