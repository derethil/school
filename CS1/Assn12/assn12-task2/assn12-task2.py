from account import Account 

def main():
    while True:
        # Ask user for starting values
        idNum = eval(input("Enter your account ID: "))
        startingBalance = eval(input("Enter your starting balance: ").strip('$'))
        annualInterestRate = eval(input("Enter your annual interest rate: ").strip('%'))

        # Check for valid starting values
        if idNum < 0 or startingBalance < 0 or annualInterestRate < 0 or annualInterestRate > 10:
            print("Invalid account details. Please input again: ")
        else:
            break

    account = Account(idNum, startingBalance, annualInterestRate)

    done = False

    while not done:
        # Menu
        print("--------------------")
        print("My Account Info")
        print("--------------------")
        print("(1): Display ID")
        print("(2): Display Balance")
        print("(3): Dislay Annual Interest Rate")
        print("(4): Display Monthly Interest Rate")
        print("(5): Display Monthly Interest")
        print("(6): Withdraw Money")
        print("(7): Deposit Money")
        print("(8): Exit")
        print("--------------------")

        menu = eval(input("Enter a selection: "))

        if menu == 1:
            print(account.getId())

        elif menu == 2:
            print('$' + str(account.getBalance()))

        elif menu == 3:
            print(str(account.getAnnualInterestRate()) + '%')

        elif menu == 4:
            print(str(round(account.getMonthlyInterestRate(), 2)) + '%')

        elif menu == 5:
            print('$' + str(round(account.getMonthlyInterest(), 2)))

        elif menu == 6:
            while True:
                # WIthdraw amount
                amount = eval(input("How much would you like to withdraw? ").strip('$'))

                # Check validity
                if amount < 0:
                    print("Invalid amount. Please try again: ")
                else:
                    break

            account.withdraw(amount)
            print("Your balance is now $%s" % account.getBalance())

        elif menu == 7:
            # WIthdraw amount
            amount = eval(input("How much would you like to deposit? ").strip('$'))

            # Check validity
            if amount < 0:
                print("Invalid amount. Please try again: ")
            else:
                break

            account.deposit(amount)
            print("Your balance is now $%s" % account.getBalance())

        elif menu == 8:
            done = True

        else:
            print("Invalid menu input. Please try again: ")



main()