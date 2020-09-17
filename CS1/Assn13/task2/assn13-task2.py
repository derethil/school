from password import Password

passObj = Password()

while True:
    passObj.setPassword(input("Enter a password: "))

    if passObj.isValid():
        print("Your password is valid.")
    else:
        print(passObj.getErrorMessage())

    if input("Would you like to enter another password (y/n)? ").lower() == 'y':
        continue
    else:
        break


