# Jaren Glenn
# Assn 4
# Draws a simple snowman with Turtle including a face, a hat, arms, and buttons.

import turtle

tr = turtle.Turtle()

def drawCircle(radius, posX, posY):
    
    # The drawCircle function allows me to draw the three circles very easily. 
	# Its arguments let me specify the radius and position in one line.
    
    tr.goto(posX, posY)
    tr.pendown()
    tr.circle(radius)
    tr.penup()
    
def drawFingers(fingerPosArray):
    
    # After getting the current position (wrist postition of the arm, each finger gets drawn
    # then goes back exactly to the wrist position. The finger's coordinates are arrays.
    
    homePos = tr.pos()
    for fingerPos in fingerPosArray: 
        tr.goto(fingerPos[0], fingerPos[1])
        tr.goto(homePos)
    tr.penup()
    
#Basic setup
tr.hideturtle()
tr.speed(10)
tr.penup()

# 3 Main Circles
drawCircle(80, 0, -240)
drawCircle(60, 0, -80)
drawCircle(40, 0, 40)

# Right Arm
tr.goto(60, -20)
tr.pendown()
tr.goto(150, 0) # Arm
drawFingers([ [160,10], [160,0], [160,-10] ])

# Left Arm
tr.goto(-60,-20)
tr.pendown()
tr.goto(-150,0)
drawFingers([ [-160,10], [-160,0], [-160,-10] ])

# Face
#Left Eye
tr.goto(-15, 90)
tr.pendown()
tr.dot(4, "black")

tr.penup()

#Right Eye
tr.goto(15, 90)
tr.pendown()
tr.dot(4, "black")
tr.penup()

#Mouth
tr.goto(-17, 70)
tr.setheading(300)
tr.pendown()
tr.circle(20,120)
tr.penup()

#Top Hat
tr.goto(40,105)
tr.pendown()

tr.begin_fill()
tr.goto(-40,105)
tr.goto(-40,120)
tr.goto(40,120)
tr.goto(40,105)
tr.goto(-30,120)
tr.goto(-30,140)
tr.goto(30,140)
tr.goto(30,120)
tr.end_fill()
tr.penup()

# Buttons
for valY in [15, -15, -45]: # Draws a button at (0,15), (0,-15), and (0,-45)
    tr.goto(0,valY)
    tr.pendown()
    tr.dot(4, "black")
    tr.penup()
    
tr.penup()
