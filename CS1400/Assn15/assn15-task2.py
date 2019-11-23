def getSum(nums):
    total = 0
    for num in nums:
        total += num
    return total

def getMax(nums):
    for i in range(len(nums) - 1):
        if nums[i] > nums[i + 1]:
            nums[i], nums[i + 1] = nums[i + 1], nums[i]

    return nums[-1]

def main():
    numList = []

    done = False
    
    while not done:
        print("Current List: " + str(numList))
        userInput = input("Please enter a number (leave blank to continue): ")

        if userInput == "":
            done = True
        else:
            numList.append(int(userInput))

    print("Number of Numbers: " + str(len(numList)))
    print("Maximum: " + str(getMax(numList)))
    print("Minimum: " + str(min(numList)))
    print("Sum: " + str(getSum(numList)))
    print("Average: " + str(getSum(numList) / len(numList)))

main()