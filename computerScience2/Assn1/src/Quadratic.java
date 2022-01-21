import java.util.Scanner;

/**
 * @author Jaren Glenn
 */
public class Quadratic {
    /**
     * Calculates the roots of a quadratic equation.
     */
    public static void main(String[] args) {
        System.out.print("Enter a, b, c:");

        Scanner input = new Scanner(System.in);

        // Sets each coefficient
        float numA = input.nextFloat();
        float numB = input.nextFloat();
        float numC = input.nextFloat();

        // Calculates the roots and assigns the discriminant to a variable for later use
        double discriminant = Math.pow(numB, 2) - (4 * numA * numC);
        double r1 = (-numB + Math.sqrt(discriminant)) / (2 * numA);
        double r2 = (-numB - Math.sqrt(discriminant)) / (2 * numA);

        // Checks how many roots there are and prints them accordingly
        if (discriminant > 0) {
            System.out.println("There are two roots for the quadratic equation with these coefficients.");

            System.out.println("r1 = " + r1);
            System.out.println("r2 = " + r2);

        } else if (discriminant == 0) {
            System.out.println("There is one root for the quadratic equation with these coefficients.");

            if (r1 == r2) {
                System.out.println("r1 = " + r1);

           } else if (Double.isNaN(r2)) {
                System.out.println("r1 = " + r1);

            } else {
                System.out.println("r1 = " + r2);
            }

        } else if (discriminant < 0) {
            System.out.println("There are no roots for the quadratic equation with these coefficients.");
        }
    }
}
