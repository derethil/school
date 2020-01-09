import java.util.Scanner;

public class Quadratic {
    public static void main(String[] args) {
        System.out.print("Enter a, b, c:");

        Scanner input = new Scanner(System.in);

        float numA = input.nextFloat();
        float numB = input.nextFloat();
        float numC = input.nextFloat();

        double discriminant = Math.pow(numB, 2) - (4 * numA * numC);

        double r1 = (-numB + Math.sqrt(discriminant)) / (2 * numA);
        double r2 = (-numB - Math.sqrt(discriminant)) / (2 * numA);

        if (discriminant > 0) {
            System.out.println("There are two roots for the quadratic equation with these coefficients.");

            System.out.println("r1 = " + r1);
            System.out.println("r2 = " + r2);

        } else if (discriminant == 0) {
            System.out.println("There is one root for the quadratic equation with these coefficients.");

            if (r1 == r2) {
                System.out.println("r1 = " + r1);

           } else if (r2 == Double.NaN) {
                System.out.println("r1 = " + r1);

            } else {
                System.out.println("r1 = " + r2);
            }

        } else if (discriminant < 0) {
            System.out.println("There are no roots for the quadratic equation with these coefficients.");
        }
    }
}
