import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String expression = "";
        Scanner scanner = new Scanner(System.in);
        while (!expression.equals("stop")) {
            expression = scanner.nextLine().trim();
            if (!expression.equals("stop")) {
                double result = Calculator.evaluate(expression);
                System.out.println(result);
            }
        }
    }
}gi

