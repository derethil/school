# Jaren Glenn
# CS1400 - 2
# Assn 5 Task 2

# This program will ask the user to input a location and diameter. 
# Turtle will then draw a 4-ringed target centered on that point with that diameter.

import turtle
import tkinter

draw = turtle.Turtle()
draw.speed(5)

coord = input("Enter a coordinate location for the target (x,y): ")
diameter = input("Enter a diameter for the bullseye: ")

# Process the inputs
coord = coord.strip('()').split(',') # Strips and splits the coordinates into an array [x,y]

posX = float(coord[0])
posY = float(coord[1])

radius = float(diameter) / 2

# Draw Functions
def drawCircle(radius, color, posX, posY):
    # Draws a circle around a specified point with a specified radius and color
    posY -= radius

    draw.goto(posX, posY)
    draw.color(color)
    draw.begin_fill()

    draw.pendown()
    draw.circle(radius)
    draw.penup()

    draw.end_fill()

draw.penup()

# Draw the target
# These augmented operators are unnecessary and make the program more complex
# But for the sake of the assignment here they are

radius *= 4
drawCircle(radius, "black", posX, posY)
radius *= (3/4)
drawCircle(radius, "deep sky blue", posX, posY)
radius *= (2/3)
drawCircle(radius, "red", posX, posY)
radius *= (1/2)
drawCircle(radius, "yellow", posX, posY)



tkinter.mainloop()