import java.util.Scanner;

public class Pyramids2 {
	public static void main(String[] args) {
		System.out.print("Enter the number of lines: ");

		Scanner userInput = new Scanner(System.in);

		final int LINES = userInput.nextInt() - 1;
		final int COL_LEN = String.valueOf((int)Math.pow(2, LINES - 1)).length() + 1;
		final int TOTAL_LEN = (COL_LEN * LINES * 2) - COL_LEN;a


		for (int line = 0; line <= LINES; line ++) {

			String lineStr = "";

			for (int num = 1; num <= Math.pow(2, line); num *= 2) {
				lineStr += String.format("%" + COL_LEN + "d", num);
			}


			for (double num = Math.pow(2, line - 1); num >= 1; num /= 2) {
				lineStr += String.format("%" + COL_LEN + "d", (int)num);
			}

			int lineLen = (COL_LEN * (line + 1) * 2) - COL_LEN;
			int centerNum = (TOTAL_LEN - lineLen) / 2;


			if (centerNum < 1) {
				System.out.println(lineStr);
			} else {
				System.out.printf("%" + (TOTAL_LEN - centerNum) + "s\n", lineStr);
			}
		}
	}
}