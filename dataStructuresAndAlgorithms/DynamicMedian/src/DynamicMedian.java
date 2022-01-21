import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class DynamicMedian<E extends Comparable<E>> {
    private E currMedian;
    private int currSize;
    private final MaxLeftistHeap<E> ltMedian = new MaxLeftistHeap<>();
    private final MinSkewHeap<E> gtMedian = new MinSkewHeap<>();

    /**
     * Inserts an item into the DynamicMedian and adjusts the currMedian accordingly.
     * @param element the item to be inserted.
     */
    public void insert(E element) {
        currSize++;
        if (currMedian == null) {
            currMedian = element;
            return;
        }

        if (element.compareTo(currMedian) < 0) {
            ltMedian.insert(element);
        } else {
            gtMedian.insert(element);
        }

        if ((ltMedian.sizeOfHeap - gtMedian.sizeOfHeap) > 1) { // ltMedian is larger by more than 1
            E biggestLtMedian = ltMedian.deleteMax();
            gtMedian.insert(currMedian);
            currMedian = biggestLtMedian;

        } else if ((gtMedian.sizeOfHeap - ltMedian.sizeOfHeap) > 1) { // gtMedian is larger by more than 1
            E smallestGtMedian = gtMedian.deleteMin();
            ltMedian.insert(currMedian);
            currMedian = smallestGtMedian;
        }
    }

    /**
     * Prints the current salary.
     * @param last specifies if this is the last median to print or not
     */
    public void printCurrentMedian(boolean last) {
        DecimalFormat df = new DecimalFormat("$###,###,###");
        String currSalary = (last) ? "all" : String.valueOf(currSize);
        System.out.printf("After %s salaries, median is: %s\n", currSalary, df.format(currMedian));
    }

    /**
     * Prints the current salary and assumes it is not the last to print.
     */
    public void printCurrentMedian() {
        printCurrentMedian(false);
    }

    /**
     * Gets the current size of the dynamic median data.
     * @return the current size
     */
    public int getCurrSize() {
        return currSize;
    }

    public static void main(String[] args) {
        DynamicMedian<Integer> salariesMedian = new DynamicMedian<>();

        try {
            Scanner scanner = new Scanner(new File("resources/Salaries.txt"));
            scanner.nextLine();
            while (scanner.hasNextInt()) {
                salariesMedian.insert(scanner.nextInt());
                scanner.nextLine();
                if (salariesMedian.getCurrSize() % 25 == 0) { salariesMedian.printCurrentMedian(); }
            }
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }

        salariesMedian.printCurrentMedian(true);


    }
}
