import java.util.ArrayList;
import java.util.Arrays;

public class UnionFind {
    private int[] data;

    private int size;

    public UnionFind(int size) {
        this.data = new int[size];
        this.size = size;
        Arrays.setAll(data, i -> -1);
    }

    /**
     * Get the data of UnionFind in a string
     * @return the string representation of the data
     */
    @Override
    public String toString() { return Arrays.toString(data); }

    /**
     * Get the root of a number's group
     * @param toFind the index (number) to get the root of
     * @return the parent (until root is found)
     */
    public int find(int toFind) {
        int parent = data[toFind];
        if (parent < 0) return toFind;

        int root = find(parent);
        data[toFind] = root;

        return find(data[toFind]);
    }

    /**
     * Gets the size of a group
     * @param groupRoot the index of the group root
     * @return the number of nodes in the group
     */
    public int getGroupSize(int groupRoot) {
        return Math.abs(data[groupRoot]);
    }

    /**
     * Gets the roots of all groups in the set
     * @return the list of group roots indexes
     */
    public ArrayList<Integer> getGroupRoots() {
        ArrayList<Integer> groupRoots = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            if (data[i] < 0) groupRoots.add(i);
        }
        return groupRoots;
    }

    public int getLargestGroupSize() {
        int smallest = data[0];
        for (int node: data) {
            if (node < smallest) smallest = node;
        }
        return Math.abs(smallest);
    }

    /**
     * Union two numbers together
     * @param num1 the first number to union
     * @param num2 the second number to union
     */
    public void union(int num1, int num2) {
        int root1 = find(num1);
        int root2 = find(num2);

        if (root2 == root1) return;

        if (Math.abs(root2) > Math.abs(root1)) {
            int temp = root1;
            root1 = root2;
            root2 = temp;
        }
        data[root1] += data[root2];
        data[root2] = root1;
    }
}
