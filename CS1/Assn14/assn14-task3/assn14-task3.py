import turtle

from circle import Circle
from rectangle import Rectangle

def chooseColor(shape):
    validColor = False 
    while not validColor:
        color = input("Enter the color of your %s (red, yellow, blue, green): " % shape).lower()

        if color in ["red", "yellow", "blue", "green"]:
            validColor = True
        else:
            print("Invalid color option.")

    return color

def main():
    exit = False

    tur = turtle.Turtle()
    shapeList = []

    while not exit:
        DASH_COUNT = 35

        print('-' * DASH_COUNT)
        print("Current Shapes: %s" % [type(shape).__name__ for shape in shapeList])
        print("(1): Enter Circle")
        print("(2): Enter Rectangle")
        print("(3): Remove Shape")
        print("(4): Draw Shapes")
        print("(0): Exit")
        print('-' * DASH_COUNT)

        menuChoice = int(input("Choose an option: "))
        
        # Add a circle 
        if menuChoice == 1:
            positionStr = input("Enter the center position of your circle (x,y): ").strip('()').split(',')
            position = [float(i) for i in positionStr]

            radius = float(input("Enter your circle's radius: "))
            color = chooseColor('circle')

            shapeList.append(Circle(tur, position, radius, color))

        # Add a rectangle
        elif menuChoice == 2:
            positionStr = input("Enter the lower-left point of your rectangle (x,y): ").strip('()').split(',')
            position = [float(i) for i in positionStr]

            height = float(input("Enter the height for your rectangle: "))
            width = float(input("Enter the width for your rectangle: "))
            color = chooseColor('rectangle')

            shapeList.append(Rectangle(tur, position, height, width, color))

        # Remove shape from the list
        elif menuChoice == 3:
            print("Current Shapes (in order of creation):")

            for i, shape in enumerate(shapeList):
                print(str(i + 1) + ": " + str(type(shape).__name__))

            removeChoice = int(input("Choose a shape to remove: "))
            shapeList.pop(removeChoice - 1)

        # Draw shapes
        elif menuChoice == 4:
            tur.reset()
            for shape in shapeList:
                shape.draw()

        elif menuChoice == 0:
            exit = True

        
    turtle.done()

main()