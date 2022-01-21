
// QuadraticProbing Hash table class
//
// CONSTRUCTION: an approximate initial size or default of 101
//
// ******************PUBLIC OPERATIONS*********************
// bool insert( x )       --> Insert x
// bool remove( x )       --> Remove x
// bool contains( x )     --> Return true if x is present
// void makeEmpty( )      --> Remove all items


import java.util.Arrays;

/**
 * Probing table implementation of hash tables.
 * Note that all "matching" is based on the equals method.
 * @author Mark Allen Weiss
 */
public class HashTable<E> {
    /**
     * Construct the hash table.
     */
    public HashTable( )
    {
        this( DEFAULT_TABLE_SIZE );
    }

    /**
     * Construct the hash table.
     * @param size the approximate initial size.
     */
    public HashTable(int size) {
        allocateArray(size);
        doClear( );
    }

    /**
     * Insert into the hash table. If the item is
     * already present, do nothing.
     * Implementation issue: This routine doesn't allow you to use a lazily deleted location.  Do you see why?
     * @param x the item to insert.
     */
    public boolean insert(E x) {
        if (contains(x)) return false;

        final int MAX_LOOP = 10;

        for (int i = 0; i < MAX_LOOP; i++) {
            probeCount++;
            int pos1 = hash(x, array1);
            if (array1[pos1] == null) {
                array1[pos1] = new HashEntry<>(x);
                array1Size++;
                insertCount++;
                return true;
            }

            E y = array1[pos1].element;
            array1[pos1] = new HashEntry<>(x);
            int pos2 = hash(y, array2);

            if (array2[pos2] == null) {
                array2[pos2] = new HashEntry<>(y);
                array2Size++;
                insertCount++;
                return true;
            }

            x = array2[pos2].element;
            array2[pos2] = new HashEntry<>(y);
        }

        rehash();
        return insert(x);
    }

    public String toString (int limit) {
        StringBuilder sb = new StringBuilder();

        sb.append(toStringArray(limit, array1, "Table 1"));
        sb.append(toStringArray(limit, array2, "Table 2"));

        sb.append("\n");
        sb.append(toStringStats());
        return sb.toString();
    }

    private String toStringArray(int limit, HashEntry<E>[] array, String label) {
        StringBuilder sb = new StringBuilder();
        int ct = 0;

        float loadFactor = (float) size(array) / array.length;
        sb.append(String.format("\n%s (%s/%s) = %.2f\n", label, size(array), array.length, loadFactor));

        for (int i = 0; i < array.length && ct < limit; i++){
            if (array[i]!=null) {
                sb.append( i + ": " + array[i].element + "\n" );
                ct++;
            }
        }

        return sb.toString();
    }

    private String toStringStats() {
        StringBuilder sb = new StringBuilder();

        sb.append("insertCount: " + insertCount + "\n");
        sb.append("probeCount: " + probeCount + "\n");

        String probeInsertRatio = String.format("%.2f", ((float) probeCount) / insertCount);
        sb.append("probes/insert: " + probeInsertRatio + "\n");

        sb.append("rehashCount: " + rehashCount + "\n");

        return sb.toString();

    }

    /**
     * Expand the hash table.
     */
    private void rehash() {
        HashEntry<E>[] oldArray1 = array1;
        HashEntry<E>[] oldArray2 = array2;

        // Create a new double-sized, empty table
        allocateArray(2 * oldArray1.length);
        array2Size = 0;
        array1Size = 0;
        rehashCount++;


        // Copy table over
        for( HashEntry<E> entry : oldArray1 )
            if( entry != null )
                insert( entry.element );

        for( HashEntry<E> entry : oldArray2 )
            if( entry != null )
                insert( entry.element );
    }

    /**
     * Method that performs quadratic probing resolution.
     * @param x the item to search for.
     * @return the position where the search terminates.
     * Never returns an inactive location.
     */

    private int isAtPos(E x, HashEntry<E>[] array) {
        int currentPos = hash(x, array);
        if (array[currentPos] == null || !array[currentPos].element.equals(x)) { return -1; }
        return currentPos;
    }


    private int size(HashEntry<E>[] array) {
        if (array == array1) return array1Size;
        if (array == array2) return array2Size;
        return -1;
    }

    /**
     * Get current size.
     * @return the size.
     */
    public int size() {
        return size(array1) + size(array2);
    }

    /**
     * Get length of internal table.
     * @return the size.
     */
    public int capacity() {
        return array1.length + array2.length;
    }

    /**
     * Find an item in the hash table.
     * @param x the item to search for.
     * @return true if item is found
     */
    public boolean contains(E x) {
        return (isAtPos(x, array1) != -1 || isAtPos(x , array2) != -1);
    }

    /**
     * Find an item in the hash table.
     * @param x the item to search for.
     * @return the matching item.
     */
    public E find(E x) {
        int pos1 = isAtPos(x, array1);
        int pos2 = isAtPos(x, array2);

        if (pos1 != -1 && array1[pos1].element.equals(x))
            return array1[pos1].element;
        if (pos2 != -1 && array2[pos2].element.equals(x))
            return array2[pos2].element;

        return null;
    }

    /**
     * Make the hash table logically empty.
     */
    public void makeEmpty() {
        doClear();
    }

    private void doClear() {
        Arrays.fill(array1, null);
    }

    private int hash(E x , HashEntry<E> [] array) {
        int hashVal = x.hashCode();

        hashVal %= array.length;
        if( hashVal < 0 )
            hashVal += array.length;

        return hashVal;
    }

    private static class HashEntry<E> {
        public E  element;   // the element

        public HashEntry( E e ) {
            element = e;
        }
    }

    private static final int DEFAULT_TABLE_SIZE = 23;

    private HashEntry<E> [ ] array1; // The array of elements
    private HashEntry<E> [ ] array2;
    private int array1Size;
    private int array2Size;

    private int insertCount = 0;
    private int probeCount = 0;
    private int rehashCount = 0;

    /**
     * Internal method to allocate array.
     * @param arraySize the size of the array.
     */
    private void allocateArray(int arraySize) {
        int newPrime = nextPrime(arraySize);
        array1 = new HashEntry[newPrime];
        array2 = new HashEntry[nextPrime(newPrime + 1)];
    }

    /**
     * Internal method to find a prime number at least as large as n.
     * @param n the starting number (must be positive).
     * @return a prime number larger than or equal to n.
     *
     */
    private static int nextPrime(int n) {
        if( n % 2 == 0 ) { n++; }
        while (!isPrime( n )) { n += 2; }
        return n;
    }

    /**
     * Internal method to test if a number is prime.
     * Not an efficient algorithm.
     * @param n the number to test.
     * @return the result of the test.
     */
    private static boolean isPrime(int n) {
        if( n == 2 || n == 3 ) { return true; }
        if( n == 1 || n % 2 == 0 ) { return false; }

        for( int i = 3; i * i <= n; i += 2 )
            if( n % i == 0 ) { return false; }

        return true;
    }


    // Simple main
    public static void main(String [] args) {
        HashTable<String> H = new HashTable<>();
        long startTime = System.currentTimeMillis();


        final int NUMS = 2000;
        final int GAP  =   37;

        System.out.println( "Checking... " );
//
        for( int i = GAP; i != 0; i = (i + GAP ) % NUMS)
            H.insert(""+i);

//        // Because GAP and NUMS are mutally prime, this inserts all numbers between 0 and 1999

        System.out.println("H size is: " + H.size());
        for( int i = GAP; i != 0; i = ( i + GAP ) % NUMS )
            if(H.insert(""+i))
                System.out.println( "ERROR Find fails " + i);
//
        for(int i = 1; i < NUMS; i+=2) {
            if(H.contains( ""+i ))
                System.out.println( "ERROR OOPS!!! " +  i);
        }
//
        long endTime = System.currentTimeMillis();

        System.out.println("Elapsed time: " + (endTime - startTime));
        System.out.println("H size is: " + H.size());
        System.out.println("Array size is: " + H.capacity());
    }

}

