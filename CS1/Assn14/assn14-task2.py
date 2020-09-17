from random import randint

genList = [randint(1,10) for i in range(1,1001)]
countList = [0]*10
for num in genList:
    countList[num - 1] += 1

print(list(enumerate(countList, 1)))