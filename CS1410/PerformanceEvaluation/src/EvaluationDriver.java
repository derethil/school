import org.w3c.dom.ls.LSOutput;

import java.util.Arrays;

/**
 * Assignment 4 for CS 1410
 * This program evaluates the linear and binary searching, along
 * with comparing performance difference between the selection sort
 * and the built-in java.util.Arrays.sort.
 *
 */
public class EvaluationDriver {
    static final int MAX_VALUE = 1_000_000;
    static final int MAX_ARRAY_SIZE = 100_000;
    static final int ARRAY_INCREMENT = 20_000;
    static final int NUMBER_SEARCHES = 50_000;

    public static void main(String[] args) {
        int[] numList = generateNumbers(10,1);
        System.out.println(Arrays.toString(numList));
    }

    public static int[] generateNumbers(int howMany, int maxValue) {
        if (howMany < 1) {
            return null;
        }

        int[] numList = new int[howMany];
        for (int i = 0; i < howMany; i++) {
            numList[i] = (int) Math.round((Math.random() * maxValue));
        }
        return numList;
    }

    public static boolean linearSearch(int[] data, int search) {
        for (int num: data) {
            if (num == search) {
                return true;
            }
        }
        return false;
    }

    public static boolean binarySearch(int[] data, int search) {
        selectionSort(data);
        int low = 0;
        int high = data.length - 1;

        while (high >= low) {
            int mid = (high + low) / 2;
            if (search == data[mid]) {
                return true;
            } else if (search < data[mid]) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return false;

    }

    public static void selectionSort(int[] data) {
        for (int i = 0; i < data.length - 1; i++) {
            int index = i;
            for (int j = i + 1; j < data.length; j++)
                if (data[j] < data[index])
                    index = j;

            int smallerNumber = data[index];
            data[index] = data[i];
            data[i] = smallerNumber;
        }
    }

    public static void printSearchInfo(int itemCount, int itemsFound, int time) {
        System.out.println("Number of items       : " + itemCount);
        System.out.println("Times value was found : " + itemsFound);
        System.out.println("Total search time     : " + time + " ms");
    }

    public static void demoLinearSearchUnsorted() {
        System.out.println("--- Linear Search Timing (unsorted) ---");
        for (int itemCount = ARRAY_INCREMENT; itemCount <= MAX_ARRAY_SIZE; itemCount += ARRAY_INCREMENT) {
            long begin = System.currentTimeMillis();


        }

    }

    public static void demoLinearSearchSorted() {

    }

    public static void demoBinarySearchSelectionSort() {

    }

    public static void demoBinarySearchFastSort() {

    }
}
