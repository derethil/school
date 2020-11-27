import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void handleFile(String filename) {
        System.out.println(filename);

        Scanner scanner = getScanner("resources/" + filename);
        int size = Integer.parseInt(scanner.nextLine());
        ArrayList<int[]> attacks = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String[] columns = scanner.nextLine().split(" ");
            int[] attack = new int[2];
            attack[0] = Integer.parseInt(columns[0]);
            attack[1] = Integer.parseInt(columns[1]);
            attacks.add(attack);
        }

        Politics politics = new Politics(attacks, size);
        politics.handleAttacks();

        for (int groupRoot: politics.set.getGroupRoots()) {
            System.out.println("Group " + groupRoot + " has " + politics.set.getGroupSize(groupRoot) + " members");
        }

        System.out.println("Largest group is of size " + politics.set.getLargestGroupSize());
        System.out.println();
    }

    private static Scanner getScanner(String filename) {
        try {
            return new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public static void main(String[] args) {
        String[] filenames = {"case1.txt", "case2.txt", "case3.txt", "case4.txt", "case5.txt", "case6.txt", "case7.txt"};

        for (String filename: filenames) {
            handleFile(filename);
        }
    }
}
