import java.util.Scanner;
import java.lang.*;

public class PartTwo {
    public static void main(String[] args) {
        System.out.print("Enter the first 9 digits of an ISBN: ");

        Scanner sc = new Scanner(System.in);
        String isbn = sc.next();
        char[] digits = isbn.toCharArray();

        int total = 0;
        for (int i = 0; i < 9; i++) {
            total += digits[i] + (i + 1);
        }
        isbn += total % 11;

        System.out.println("THe ISBN-10 number is: " + isbn);
    }
}
