import java.util.Scanner;

public class Pyramids2 {
	public static void main(String[] args) {
		System.out.print("Enter the number of lines: ");
		Scanner userInput = new Scanner(System.in);

		final int LINES = userInput.nextInt();
		final int COL_LEN = String.valueOf((int)Math.pow(2, LINES)).length() + 1;
		final int TOTAL_LEN = (COL_LEN * LINES * 2) - COL_LEN;

		for (int line = 1; line <= LINES; line ++) {

			String formattedString = "";

			for (int num = 1; num <= line; num++) {
				formattedString += String.format("%" + COL_LEN + "d", (int)Math.pow(2, num - 1));
			}

			for (int num = line - 1; num > 0; num--) {
				formattedString += String.format("%" + COL_LEN + "d", (int)Math.pow(2, num - 1));
			}

			int lineLen = (COL_LEN * line * 2) - COL_LEN;
			int centerNum = (TOTAL_LEN - lineLen) / 2;
			int formatNum = centerNum == 0 ? TOTAL_LEN : TOTAL_LEN - centerNum;

			System.out.printf("%" + formatNum + "s\n", formattedString);

		}
	}
}