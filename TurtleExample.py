import turtle

tr = turtle.Turtle()

tr.circle(150)

tr.penup()
tr.goto(-50, 175)

tr.pendown()
tr.circle(25)
tr.penup()

tr.goto(50, 175)
tr.pendown()
tr.circle(25)

# draw smile
tr.penup()
tr.goto(-60, 60)
tr.setheading(315)
tr.pendown()
tr.width(10)
tr.circle(90, 90)

turtle.mainloop()