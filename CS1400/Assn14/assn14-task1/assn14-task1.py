'''
Jaren Glenn
CS1400 - 2
Assn 14 - Task 1
'''
from cuboid import Cuboid

def main():
    # Get Cuboid 1 information from user
    dims1 = input("Enter the dimensions for Cuboid 1 (length, width, height): ").split(',')
    dimsFloat = [float(i) for i in dims1] 
    cuboid1 = Cuboid(dimsFloat[0], dimsFloat[1], dimsFloat[2])

    # Get Cuboid 2 information from user
    dims2 = input("Enter the dimensions for Cuboid 2 (length, width, height): ").split(',')
    dimsFloat = [float(i) for i in dims2] 
    cuboid2 = Cuboid(dimsFloat[0], dimsFloat[1], dimsFloat[2])


    # Print operator overloader outputs
    DASH_COUNT = 35

    print('-'*DASH_COUNT)
    dunderStr = format("Cuboid 1 < Cuboid 2:", "<25s") + format(str(cuboid1 < cuboid2), ">5s") + "\n"
    dunderStr += format("Cuboid 1 > Cuboid 2:", "<25s") + format(str(cuboid1 > cuboid2), ">5s") + "\n"
    dunderStr += format("Cuboid 1 == Cuboid 2:", "<25s") + format(str(cuboid1 == cuboid2), ">5s") + "\n"
    dunderStr += format("Cuboid 1 len() Output:", "<25s") + format(str(len(cuboid1)), ">5s") + "\n"
    dunderStr += format("Cuboid 2 len() Output:", "<25s") + format(str(len(cuboid2)), ">5s")
    print(dunderStr)

    print('-'*DASH_COUNT + "\nCuboid 1 + Cuboid 2\n" + '-' * DASH_COUNT)
    print(cuboid1 + cuboid2)

    print('-'*DASH_COUNT + "\nCuboid 1 - Cuboid 2\n" + '-' * DASH_COUNT)
    print(cuboid1 - cuboid2)
    print("-"*DASH_COUNT)

main()