from cuboid import Cuboid

def main():
    dims1 = input("Enter the dimensions for Cuboid 1 (length, width, height): ").split(',')
    dimsFloat = [float(i) for i in dims1] 
    cuboid1 = Cuboid(dimsFloat[0], dimsFloat[1], dimsFloat[2])

    dims2 = input("Enter the dimensions for Cuboid 2 (length, width, height): ").split(',')
    dimsFloat = [float(i) for i in dims2] 
    cuboid2 = Cuboid(dimsFloat[0], dimsFloat[1], dimsFloat[2])

    DASH_COUNT = 35

    print('-'*DASH_COUNT)

    if cuboid1 == cuboid2:
        print("The two cuboids have the same volume.")
    elif cuboid1 > cuboid2:
        print("Cuboid 1 is bigger than Cuboid 2.")
    elif cuboid1 < cuboid2:
        print("Cuboid 2 is bigger than Cuboid 1.")

    print('-'*DASH_COUNT + "\nCuboid 1 + Cuboid 2")
    print(cuboid1 + cuboid2)
    print("-"*DASH_COUNT)


    
main()