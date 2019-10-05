'''
Jaren Glenn
CS1400
Assn 7 - 2

See system analysis for description.

Software Development Lifecycle

System Analysis
This program will ask the user to input the location and radius of two circles.
It then will calculate whether one circle is inside the other, if they are overlapping,
or if they are not overlapping at all.

System Design
1. Ask user for the details of circle A
2. Ask user for the details of circle B
3. Convert input to integers
4. Calculate the distance between the the centers using sqrt(|aX - bX| ** 2 + |aY - bY| ** 2)
5. Compare the distance to the radius of the circles
   a. If distance <= rA - rB then the circles are inside each other
   b. If distance <= rA + rB then the circles are overlapping
   c. Else they are not touching
6. Print the output of the comparison

Testing
Test #1:
   Input:
      Circle A = (0.5, 5.1), radius = 13
      Circle B = (1,, 1.7), radius = 4.5
   Output:
      Circle B is inside Circle A
Test #2:
   Input: 
      Circle A = (4.4, 5.7), radius = 5.5
      Circle B = (6.7, 3.5), radius = 3
   Output:
      Circle B overlaps circle A
Test #3:
   Input:
      Circle A = (4.4, 5.5), radius = 1
      Circle B = (5.5, 7.2), radius = 1
    Outout:
      Circle B does not overlap circle A  
'''
from math import sqrt

# Get circle info
circleA = input("Please enter circle A's x,y coordinates and radius <x,y,r>: ").split(',')
circleB = input("Please enter circle B's x,y coordinates and radius <x,y,r>: ").split(',')

# Convert each number to an int
for num in circleA:
    circleA[circleA.index(num)] = float(num)

for num in circleB:
    circleB[circleB.index(num)] = float(num) 


# Calculate distance between the centers of the circles
distance = sqrt(4)

print(distance)