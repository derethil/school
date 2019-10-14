'''
    This is the starter file. Only fill in the areas indicated.
    Do not modify existing code.
    Replace this file header with the normal file header (name, etc)
'''

#### Add Import Statement(s) as needed ####
from chessboard import drawChessboard
#### End Add Import Statement(s) ####

def main():
    #### Add Code to get input from user ####
    
    startCoord = input("Enter an (x,y) coordinate to draw the chessboard: ").strip('()').split(',')
    startX, startY = int(startCoord[0]), int(startCoord[1])

    width = input("Enter a width value (leave blank for default): ")
    height = input("Enter a height value (leave blank for default): ")

    #### End Add Code to get input from user ####

    if width == "" and height == "":
        drawChessboard(startX, startY)
    elif height == "":
        drawChessboard(startX, startY, width=eval(width))
    elif width == "":
        drawChessboard(startX, startY, height=eval(height))
    else:
        drawChessboard(startX, startY, eval(width), eval(height))


main()