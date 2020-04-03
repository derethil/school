import java.lang.Math;

public class Recursion {
    public static void main(String[] args) {

        int[] sumMe = { 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89 };
        System.out.printf("Array Sum: %d\n", arraySum(sumMe, 0));

        int[] minMe = { 0, 1, 1, 2, 3, 5, 8, -42, 13, 21, 34, 55, 89 };
        System.out.printf("Array Min: %d\n", arrayMin(minMe, 0));

        String[] amISymmetric =  {
                "You can cage a swallow can't you but you can't swallow a cage can you",
                "I still say cS 1410 is my favorite class"
        };
        for (String test : amISymmetric) {
            String[] words = test.toLowerCase().split(" ");
            System.out.println();
            System.out.println(test);
            System.out.printf("Is word symmetric: %b\n", isWordSymmetric(words, 0, words.length - 1));
        }

        double weights[][] = {
                            { 51.18 },
                        { 55.90, 131.25 },
                    { 69.05, 133.66, 132.82 },
                { 53.43, 139.61, 134.06, 121.63 }
        };
        System.out.println();
        System.out.println("--- Weight Pyramid ---");
        for (int row = 0; row < weights.length; row++) {
            for (int column = 0; column < weights[row].length; column++) {
                double weight = computePyramidWeights(weights, row, column);
                System.out.printf("%.2f ", weight);
            }
            System.out.println();
        }

        char image[][] = {
                {'*', '*', ' ', ' ', ' ', ' ', ' ', ' ', '*', ' '},
                {' ', '*', ' ', ' ', ' ', ' ', ' ', ' ', '*', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', '*', '*', ' ', ' '},
                {' ', '*', ' ', ' ', '*', '*', '*', ' ', ' ', ' '},
                {' ', '*', '*', ' ', '*', ' ', '*', ' ', '*', ' '},
                {' ', '*', '*', ' ', '*', '*', '*', '*', '*', '*'},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '*', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '*', ' '},
                {' ', ' ', ' ', '*', '*', '*', ' ', ' ', '*', ' '},
                {' ', ' ', ' ', ' ', ' ', '*', ' ', ' ', '*', ' '}
        };

        int howMany = howManyOrganisms(image);
        System.out.println();
        System.out.println("--- Labeled Organism Image ---");
        for (char[] line : image) {
            for (char item : line) {
                System.out.print(item);
            }
            System.out.println();
        }
        System.out.printf("There are %d organisms in the image.\n", howMany);
    }

    public static boolean isWordSymmetric(String[] words, int start, int end) {
        if (start >= end) {
            return true;
        }
        if (words[start].equalsIgnoreCase(words[end])) {
            return isWordSymmetric(words, start + 1, end - 1);
        }
        return false;
    }

    public static long arraySum(int[] data, int position) {
        if (data.length == 0 || position >= data.length) {
            return 0;
        }
        if (position == data.length - 1) {
            return data[position];
        }
        return data[position] + arraySum(data, position + 1);
    }

    public static int arrayMin(int[] data, int position) {
        if (position == data.length - 1) {
            return data[position];
        }
        return Math.min(data[position], arrayMin(data, position + 1));
    }

    public static double computePyramidWeights(double[][] weights, int row, int column) {
        if (row < 0 || row >= weights.length || column < 0 || column >= weights[row].length || weights[row].length == 0) {
            return 0;
        }
        if (row == 0) {
            return weights[0][0];
        }
        if (column == 0) {
            return weights[row][column] + (0.5 * computePyramidWeights(weights, row - 1, column));
        } else if (column == weights[row].length - 1) {
            return weights[row][column] + (0.5 * computePyramidWeights(weights, row - 1, column - 1));
        } else {
            double aboveLeft = computePyramidWeights(weights, row - 1, column - 1);
            double aboveRight = computePyramidWeights(weights, row - 1, column);
            return weights[row][column] + (0.5 * aboveLeft) + (0.5 * aboveRight);
        }
    }

    public static int howManyOrganisms(char[][] image) {
        int numberOfOrganisms = 0;

        for (int row = 0; row < image.length; row++) {
            for (int column = 0; column < image[row].length; column++) {
                if (markOrganism(image, row, column, (char)(numberOfOrganisms + 97))) {
                    numberOfOrganisms++;
                }
            }
        }
        return numberOfOrganisms;
    }

    private static boolean markOrganism(char[][] image, int row, int column, char marker) {
        if (image[row][column] == '*') {
            image[row][column] = marker;

            if (row - 1 >= 0 && image[row -1].length > column) { markOrganism(image, row - 1, column, marker); }                // Top
            if (row + 1 < image.length && image[row + 1].length > column) { markOrganism(image, row + 1, column, marker); }     // Bottom
            if (column - 1 > 0) { markOrganism(image, row, column - 1, marker); }                                            // Left
            if (column + 1 < image[row].length) { markOrganism(image, row, column + 1, marker); }                            // Right

            return true;
        }
        return false;
    }
}