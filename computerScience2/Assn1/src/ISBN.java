import java.util.Scanner;

/**
 * @author Jaren Glenn
 */
public class ISBN {
	/**
	 * Calculates the 10th digit of a user-inputted 9-digit ISBN.
	 *
	 */
	public static void main(String[] args) {
		System.out.print("Enter the first 9 digits of an ISBN: ");

		// Get 9 ISBN input
		Scanner sc = new Scanner(System.in);
		final int INPUT = sc.nextInt();
		int start = INPUT;

		// Split the ISBN into an array for easy algebraic manipulation
		int digits[] = new int[9];

		for (int i = 8; i >= 0; i--) {
			digits[i] = start % 10;
			start /= 10;
		}

		// Calculate the 10th digit
		int total = 0;
		for (int i = 0; i < 9; i++) {
			total += digits[i] * (i + 1);
		}

		// Print out the final ISBN - and make sure the final digit is X in replacement of 10
		System.out.println("The ISBN-10 number is: " + String.format("%09d", INPUT) + (total % 11 != 10 ? total % 11 : "X"));
	}
}
