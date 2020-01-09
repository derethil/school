import java.util.Scanner;
import java.util.Arrays;

public class ISBN {
    public static void main(String[] args) {
        System.out.print("Enter the first 9 digits of an ISBN: ");

        Scanner sc = new Scanner(System.in);
        int start, input;
        input = start = sc.nextInt();

        int digits[] = new int[9];

        for (int i = 8; i >= 0; i--) {
            digits[i] = start % 10;
            start /= 10;
        }

        int total = 0;

        for (int i = 0; i < 9; i++) {
            total += digits[i] * (i + 1);
        }

        System.out.println("The ISBN-10 number is: " + String.format("%09d", input) + (total % 11 != 10 ? total % 11 : "X"));
    }
}
