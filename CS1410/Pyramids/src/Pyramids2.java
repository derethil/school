import java.util.Scanner;

public class Pyramids2 {
	public static void main(String[] args) {
		System.out.print("Enter the number of lines: ");
		Scanner userInput = new Scanner(System.in);

		final int LINES = userInput.nextInt();
		final int COL_LEN = String.valueOf((int)Math.pow(2, LINES)).length() + 1;
		final int TOTAL_LEN = (COL_LEN * LINES * 2) - COL_LEN;

		for (int line = 1; line <= LINES; line ++) {

			String lineStr = "";

			for (int num = 1; num <= line; num++) {
				lineStr += String.format("%" + COL_LEN + "d", (int)Math.pow(2, num - 1));
			}

			for (int num = line - 1; num > 0; num--) {
				lineStr += String.format("%" + COL_LEN + "d", (int)Math.pow(2, num - 1));
			}

			int lineLen = (COL_LEN * line * 2) - COL_LEN;
			int centerNum = (TOTAL_LEN - lineLen) / 2;

			if (centerNum == 0) {
				System.out.println(lineStr);
			} else {
				System.out.printf("%" + (TOTAL_LEN - centerNum) + "s\n", lineStr);
			}
		}
	}
}