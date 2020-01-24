import java.util.Scanner;

public class Pyramids1 {
    public static void main(String[] args) {
        System.out.print("Enter the number of lines: ");

        Scanner userInput = new Scanner(System.in);

        final int LINES = userInput.nextInt();
        final int COL_LEN = String.valueOf(LINES).length() + 1;
        final int TOTAL_LEN = (COL_LEN * LINES * 2) + 1;

        for (int line = 1; line <= LINES; line ++) {

            for (int num = line; num > 0; num--) {
                System.out.printf("%" + COL_LEN + "d", num);
            }

            for (int num = 2; num <= line; num++) {
                System.out.printf("%" + COL_LEN + "d", num);
            }

            System.out.print("\n");
        }
    }
}