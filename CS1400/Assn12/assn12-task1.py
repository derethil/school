import turtle
tr = turtle.Turtle()

class Face:
    def __init__(self):
        self.__smile = True
        self.__happy = True
        self.__dark_eyes = True

    def draw_face(self):
        tr.clear()
        self.__draw_head()
        self.__draw_eyes()
        self.__draw_mouth()

    def is_smile(self):
        return self.__smile

    def is_happy(self):
        return self.__happy

    def is_dark_eyes(self):
        return self.__dark_eyes

    def __draw_head(self):
        # Change face color
        if self.is_happy():
            tr.fillcolor("yellow")
        else:
            tr.fillcolor("red")

        # Reset tr to normal
        tr.setheading(0)
        tr.speed(10)
        tr.penup()
        tr.width(0.5)

        # Draw head
        tr.setpos(0, -100)
        tr.begin_fill()
        tr.pendown()
        tr.circle(100)
        tr.penup()
        tr.end_fill()

    def __draw_eyes(self):
        # Set eye color
        if self.is_dark_eyes():
            tr.fillcolor("black")
        else:
            tr.fillcolor("blue")

        # Draw eyes
        for i in [-30,30]:
            tr.setpos(i, 30)
            tr.begin_fill()
            tr.pendown()
            tr.circle(15)
            tr.penup()
            tr.end_fill()

    def __draw_mouth(self):
        # Set smile type
        if self.is_smile():
            tr.goto(-56,-45)
            tr.setheading(-45)
        else:
            tr.goto(56,-45)
            tr.setheading(135)

        # Draw smile
        tr.width(5)
        tr.pendown()
        tr.circle(80,90)
        tr.penup()

    def set_mouth(self, smile):
        self.__smile = smile

    def set_emotion(self, happy):
        self.__happy = happy

    def set_eyes(self, dark_eyes):
        self.__dark_eyes = dark_eyes

    def change_mouth(self):
        self.set_mouth(not self.is_smile())
        self.draw_face()

    def change_emotion(self):
        self.set_emotion(not self.is_happy())
        self.draw_face()

    def change_eyes(self):
        self.set_eyes(not self.is_dark_eyes())
        self.draw_face()


def main():
    face = Face()
    face.draw_face()

    done = False

    while not done:
        print("Change My Face")
        mouth = "frown" if face.is_smile() == True else "smile"
        emotion = "angry" if face.is_happy() == True else "happy"
        eyes = "blue" if face.is_dark_eyes() == True else "black"
        print("1) Make me", mouth)
        print("2) Make me", emotion)
        print("3) Make my eyes", eyes)
        print("0) Quit")

        menu = eval(input("Enter a selection: "))

        if menu == 1:
            face.change_mouth()
        elif menu == 2:
            face.change_emotion()
        elif menu == 3:
            face.change_eyes()
        else:
            break

    print("Thanks for Playing")

    tr.hideturtle()
    turtle.done()


main()
