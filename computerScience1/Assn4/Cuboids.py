# Jaren Glenn
# Assn 4
# Reads the dimensions of a cuboid (input by user) and prints the volume and surface area.

from math import isnan # For some reason 

print("Welcome to the Cuboid Calculator!")
print("Please enter the values in feet.")

def getDimensions():
    global length, width, height # Allows me to access the variables after the function has been called.
    length = input("Enter the length: ") 
    width = input("Enter the width: ")
    height = input("Enter the height: ")

    try: 
        length = int(length)
        width = int(width)
        height = int(height)
    except ValueError: # If the data cannot be evaluated to an int, it will throw a ValueError and test for floating points.
        try:
            length = float(length)
            width = float(width)
            height = float(height)
        except: # If the data cannot be evaluated to either a int or float, it will recursively call the function, asking the user to input your dimensions again
            print("One of your dimensions is in an invalid format. Try again: ")
            getDimensions()

getDimensions()

# Calculates the  volume and surface area of the cuboid
volume = length * width * height
surfaceArea = 2 * ((length * width) + (length * height) + (width * height))

print("Your %s X %s X %s cuboid has a volume of %s cubic feet and a surface area of %s square feet." % (
    length, 
    width, 
    height, 
    round(volume, 2), # These two round functions will round the answer to the nearest .01. This makes floats a lot easier to read.
    round(surfaceArea, 2)
    ))
