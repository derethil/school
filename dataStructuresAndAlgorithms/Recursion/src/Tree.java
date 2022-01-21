// ******************ERRORS********************************
// Throws UnderflowException as appropriate

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.BinaryOperator;

class UnderflowException extends RuntimeException {
    /**
     * Construct this exception object.
     *
     * @param message the error message.
     */
    public UnderflowException(String message) {
        super(message);
    }
}

public class Tree {
    final String ENDLINE = "\n";
    private BinaryNode root;  // Root of tree
    private BinaryNode curr;  // Last node accessed in tree
    private String treeName;     // Name of tree


    /**
     * Create an empty tree
     *
     * @param label Name of tree
     */
    public Tree(String label) {
        treeName = label;
        root = null;
    }

    /**
     * Create BST from Array
     *
     * @param arr   List of elements to be added
     * @param label Name of  tree
     */
    public Tree(int[] arr, String label) {
        root = null;
        treeName = label;
        for (int i = 0; i < arr.length; i++) {
            bstInsert(arr[i]);
        }
    }


    // Complexity of O(nlogn)
    public static void getLeaves(int[] preorder, int beg, int end, ArrayList<Integer> leaves) {
        if (beg == end) {
            leaves.add(preorder[beg]);
            return;
        }
        int leftMaxIdx = beg;
        for (int i = beg; i <= end; i++) {
            if (preorder[i] > preorder[beg]) {
                leftMaxIdx = i - 1;
                break;
            }
        }

        if (leftMaxIdx != beg) { getLeaves(preorder, beg + 1, leftMaxIdx, leaves); }
        getLeaves(preorder, leftMaxIdx + 1, end, leaves);
    }



    /**
     * Create Tree By Level.  Parents are set.
     * This runs in  O(n)
     * @param arr   List of elements to be added
     * @param label Name of tree
     */
    public void createTreeByLevel(int[] arr, String label) {
        treeName = label;
        if (arr.length <= 0) {
            root = null;
            return;
        }

        ArrayList<BinaryNode> nodes = new ArrayList<BinaryNode>();
        root = new BinaryNode(arr[0]);
        nodes.add(root);
        BinaryNode newr = null;
        for (int i = 1; i < arr.length; i += 2) {
            BinaryNode curr = nodes.remove(0);
            BinaryNode newl = new BinaryNode(arr[i], null, null, curr);
            nodes.add(newl);
            newr = null;
            if (i + 1 < arr.length) {
                newr = new BinaryNode(arr[i + 1], null, null, curr);
                nodes.add(newr);
            }

            curr.left = newl;
            curr.right = newr;
        }
    }

    /**
     * Change name of tree
     *
     * @param name new name of tree
     */
    public void changeName(String name) {
        this.treeName = name;
    }

    /**
     * This runs in O(n)
     * Return a string displaying the tree contents as a tree with one node per line
     */
    public String toString() {
        if (root == null)
            return (treeName + " Empty tree\n");
        else
            return treeName + "\n" + toString(root, 0);
    }

    private String toString(BinaryNode node, int height) {
        if (node == null) { return ""; }
        final String INDENT = "  ";
        StringBuilder sb = new StringBuilder();

        sb.append(toString(node.right, height + 1));

        sb.append(INDENT.repeat(height));
        sb.append(node.toString());
        sb.append("\n");

        sb.append(toString(node.left, height + 1));

        return sb.toString();
    }

    /**
     * This runs in  O(n)
     * Return a string displaying the tree contents as a single line
     */
    public String toString2() {
        if (root == null)
            return treeName + " Empty tree";
        else
            return treeName + " " + toString2(root);
    }

    /**
     * Find successor of "curr" node in tree
     * This runs in O(n)
     * @return String representation of the successor
     */
    public String successor() {
        if (curr == null) curr = root;
        curr = successor(curr, curr);
        if (curr == null) return "null";
        else return curr.toString();
    }

    private BinaryNode successor(BinaryNode succFrom, BinaryNode currNode) {
        if (currNode.element > succFrom.element) {
            return currNode;
        }
        else if (currNode.right != null && currNode.right.element > succFrom.element) {
            return findLeftmost(currNode.right);
        } else {
            return successor(succFrom, currNode.parent);
        }
    }

    private BinaryNode findLeftmost(BinaryNode currentLeftmost) {
        while (currentLeftmost.left != null) {
            currentLeftmost = currentLeftmost.left;
        }
        return currentLeftmost;
    }

    /**
     * Print all paths from root to leaves
     * Complexity of O(n)
     */
    public void printAllPaths() {
        printAllPaths(root, "");
    }

    private void printAllPaths(BinaryNode node, String currPath) {
        if (node == null) { return; }
        currPath = currPath + node.element + " ";

        if (node.left != null && node.right != null) {
            printAllPaths(node.left, currPath);
            printAllPaths(node.right, currPath);
        } else if (node.left == null && node.right == null) {
            System.out.println(currPath.toString());
        } else if (node.left != null) {
            printAllPaths(node.left, currPath);
        } else {
            printAllPaths(node.right, currPath);
        }

    }

    /**
     * Counts all non-null binary search trees embedded in tree
     * This runs in O(n)
     * @return Count of embedded binary search trees
     */
    public Integer countBST() {
        if (root == null) return 0;
        return countBST(root);
    }

    private int countBST(BinaryNode currRoot) {
        if (currRoot == null) { return 0; }
        if (currRoot.left == null && currRoot.right == null) { return 1; }

        int leftRightCount = countBST(currRoot.left) + countBST(currRoot.right);
        if (treeMax(currRoot.left) < currRoot.element && treeMax(currRoot.right) > currRoot.element) {
            return leftRightCount + 1;
        }
        return leftRightCount;
    }
    // This runs in O(n)
    private int treeMax(BinaryNode node) {
        if (node == null) { return 0; }

        int max = node.element;

        if(node.left != null) {
            max = Math.max(max, treeMax(node.left));
        }
        if (node.right != null) {
            max = Math.max(max, treeMax(node.right));
        }
        return max;
    }

    /**
     * Insert into a bst tree; duplicates are allowed
     *
     * @param x the item to insert.
     */
    public void bstInsert(int x) {
        root = bstInsert(x, root, null);
    }

    /**
     * Determines if item is in tree
     *
     * @param item the item to search for.
     * @return true if found.
     */
    public boolean contains(int item) {

        return bstContains(item, root);
    }

    /**
     * Remove all paths from tree that sum to less than given value
     * Complexity of O(n)
     * @param sum: minimum path sum allowed in final tree
     */
    public void pruneK(int sum) {
        pruneK(root, sum, 0);
    }

    private BinaryNode pruneK(BinaryNode currNode, int sumTo, int partialSum) {
        if (currNode == null) { return null; }

        currNode.left = pruneK(currNode.left, sumTo, partialSum + currNode.element);
        currNode.right = pruneK(currNode.right, sumTo, partialSum + currNode.element);

        if (partialSum + currNode.element < sumTo && (currNode.left == null && currNode.right == null)) {
            return null;
        }

        return currNode;
    }

    /**
     * Find the least common ancestor of two nodes
     * Complexity of O(logn)
     * @param a first node
     * @param b second node
     * @return String representation of ancestor
     */
    public String lca(int a, int b) {
        BinaryNode ancestor = null;
        if (a < b) {
            ancestor = lca(root, a, b);
        } else {
            ancestor = lca(root, b, a);
        }
        if (ancestor == null) return "none";
        else return ancestor.toString();
    }

    private BinaryNode lca(BinaryNode currNode, int a, int b) {
        if (bstContains(a, currNode.left) && bstContains(b, currNode.left)) {
            return lca(currNode.left, a, b);
        } else if (bstContains(a, currNode.right) && bstContains(b, currNode.right)) {
            return lca(currNode.right, a, b);
        } else {
            return currNode;
        }
    }

    /**
     * Balance the tree
     * O(n) complexity
     */
    public void balanceTree() {
        List<BinaryNode> sortedNodes = getSortedData(root);
        root = balanceTree(sortedNodes, 0, sortedNodes.size() - 1, null);
    }

    private BinaryNode balanceTree(List<BinaryNode> sortedNodes, int startIdx, int endIdx, BinaryNode parent) {
        if (startIdx > endIdx) { return null; }
        int midIdx = (startIdx + endIdx) / 2;
        BinaryNode currNode = sortedNodes.get(midIdx);
        currNode.parent = parent;
        currNode.left = balanceTree(sortedNodes, startIdx, midIdx - 1, currNode);
        currNode.right = balanceTree(sortedNodes, midIdx + 1, endIdx, currNode);
        return sortedNodes.get(midIdx);
    }

    private List<BinaryNode> getSortedData(BinaryNode currNode) {
        List<BinaryNode> ordered = new ArrayList<>();
        if (currNode.left != null) { ordered.addAll(getSortedData(currNode.left)); }
        ordered.add(currNode);
        if (currNode.right != null) { ordered.addAll(getSortedData(currNode.right)); }
        return ordered;
    }

    /**
     * In a BST, keep only nodes between range
     *
     * @param min lowest value
     * @param max highest value
     */
    public void keepRange(int min, int max) {
        root = keepRange(min, max, root);
    }

    private BinaryNode keepRange(int min, int max, BinaryNode currNode) {
        if (currNode == null) { return null; }

        currNode.left = keepRange(min, max, currNode.left);
        currNode.right = keepRange(min, max, currNode.right);

        if (currNode.element < min) {
            return currNode.right;
        } else if (currNode.element > max) {
            return currNode.left;
        }
        return currNode;
    }

    /**
     * Complexity of O(n^2)
     * @return for the level with maximum sum, print the sum of the nodes
     */
    public int maxLevelSum() {
        int height = 1;
        int maxHeight = findHeight(root);
        int currMaxSum = 0;

        int sum;
        while (height < maxHeight) {
            sum = levelSum(root, height);
            if (sum > currMaxSum) { currMaxSum = sum; }
            height++;
        }
        return currMaxSum;
    }

    private int levelSum(BinaryNode root, int height) {
        if (root == null) { return 0; }
        if (height == 1) {
            return root.element;
        }

        int left = levelSum(root.left, height - 1);
        int right = levelSum(root.right, height - 1);
        return left + right;
    }

    private int findHeight(BinaryNode currNode) {
        if (currNode == null) return 0;
        int leftHeight = findHeight(currNode.left);
        int rightHeight = findHeight(currNode.right);
        return Math.max(leftHeight, rightHeight) + 1;
    }


    /**
     * @return the sum of the maximum path between any two leaves
     */
    public Integer maxPath() {
        return maxPathSum();
    }

    /**
     * @param preorder traversal of a BST,
     *                 print the leaves (without creating the tree)
     */
    public void printLeaves(int[] preorder) {
    }

    /**
     * @return true if the tree is a Sum Tree (every non-leaf node is sum of nodes in subtree)
     * Complexity of O(n)
     */
    public boolean isSum() {
        int sum = getSum(root);
        return (sum >= 0 && sum == root.element);

    }

    private int getSum(BinaryNode node) {
        if (node.right == null && node.left == null) { return node.element; }
        int childrenSum = getSum(node.left) + getSum(node.right);

        if (node == root) { return childrenSum; }
        return childrenSum == node.element ? childrenSum + node.element : -99999;
    }

    /**
     * Complexity of O(n^2))
     * @return largest path value for any path between two leaves
     */
    public int maxPathSum() {
        StaticInt sum = new StaticInt(0);
        maxPathSum(root, sum);
        return sum.get();
    }

    private int maxPathSum(BinaryNode node, StaticInt maxSum) {
        if (node == null) { return 0; }

        int leftMaxSum = maxPathSum(node.left, maxSum);
        int rightMaxSum = maxPathSum(node.right, maxSum);

        int currMaxSum = leftMaxSum + rightMaxSum + node.element;

        if (currMaxSum > maxSum.get()) { maxSum.set(currMaxSum); }

        if (node.left == null) { return rightMaxSum + node.element; }
        if (node.right == null) { return leftMaxSum + node.element; }
        return Math.max(leftMaxSum, rightMaxSum) + node.element;
    }

    private static class StaticInt {
        private int maxSum;
        public StaticInt(int startValue) {
            maxSum = startValue;
        }
        public int  increment() { return maxSum++; }
        public void set(int sum) { this.maxSum = sum; }
        public int get() { return maxSum; }
    }


    //PRIVATE

    /**
     * @param preorderT  preorder traversal of tree
     * @param postorderT postorder traversal of tree
     * @param name       of tree
     *                   create a full tree (every node has 0 or 2 children) from its traversals.  This is not a BST.
     */
    public void createTreeTraversals(int[] preorderT, int[] postorderT, String name) {
        this.treeName = name;
        root = createTreeTraversals(null, preorderT, postorderT);
    }

    private BinaryNode createTreeTraversals(BinaryNode parent, int [] preorder, int[] postorder) {
        if (preorder.length == 0) return null;
        BinaryNode currNode = new BinaryNode(preorder[0]);
        currNode.parent = parent;
        if (preorder.length == 1) return currNode;

        int leftNodesIdx = 0;
        while (postorder[leftNodesIdx] != preorder[1]) {
            leftNodesIdx++;
        }
        leftNodesIdx++;

        currNode.left = createTreeTraversals(currNode, Arrays.copyOfRange(preorder, 1, leftNodesIdx+1), Arrays.copyOfRange(postorder, 0, leftNodesIdx));
        currNode.right = createTreeTraversals(currNode, Arrays.copyOfRange(preorder, leftNodesIdx+1, preorder.length), Arrays.copyOfRange(postorder, leftNodesIdx, preorder.length-1));

        return currNode;
    }


    /**
     * Internal method to insert into a subtree.
     * In tree is balanced, this routine runs in O(log n)
     *
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode bstInsert(int x, BinaryNode t, BinaryNode parent) {
        if (t == null)
            return new BinaryNode(x, null, null, parent);

        if (x < t.element) {
            t.left = bstInsert(x, t.left, t);
        } else {
            t.right = bstInsert(x, t.right, t);
        }

        return t;
    }

    /**
     * Internal method to find an item in a subtree.
     * This routine runs in O(log n) as there is only one recursive call that is executed and the work
     * associated with a single call is independent of the size of the tree: a=1, b=2, k=0
     *
     * @param x is item to search for.
     * @param t the node that roots the subtree.
     *          SIDE EFFECT: Sets local variable curr to be the node that is found
     * @return node containing the matched item.
     */
    private boolean bstContains(int x, BinaryNode t) {
        curr = null;
        if (t == null)
            return false;

        if (x < t.element)
            return bstContains(x, t.left);
        else if (x > t.element)
            return bstContains(x, t.right);
        else {
            curr = t;
            return true;    // Match
        }
    }

    /**
     * Internal method to return a string of items in the tree in order
     * This routine runs in O(??)
     *
     * @param t the node that roots the subtree.
     */
    private String toString2(BinaryNode t) {
        if (t == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(toString2(t.left));
        sb.append(t.element + " ");
        sb.append(toString2(t.right));
        return sb.toString();
    }

    /**
     * Internal method to return a string of items in the tree in order
     * This routine runs in O(??)
     *
     * @param t the node that roots the subtree.
     */
    private String toString(BinaryNode t, String indent) {
        return "Need print pretty";
    }

    private static class BinaryNode {
        int element;            // The data in the node
        BinaryNode left;   // Left child
        BinaryNode right;  // Right child
        BinaryNode parent; //  Parent node

        // Constructors
        BinaryNode(int theElement) {
            this(theElement, null, null, null);
        }

        BinaryNode(int theElement, BinaryNode lt, BinaryNode rt, BinaryNode pt) {
            element = theElement;
            left = lt;
            right = rt;
            parent = pt;
        }


        public String toString() {
            StringBuilder sb = new StringBuilder();
//            sb.append("Node:");
            sb.append(element);
            if (parent == null) {
                sb.append("[]");
            } else {
                sb.append("[");
                sb.append(parent.element);
                sb.append("]");
            }

            return sb.toString();
        }

    }

    // Test program
    public static void main(String[] args) {

        final String ENDLINE = "\n";


        // Assignment Problem 1
        int[] v1 = {25, 10, 60, 55, 58, 56, 14, 63, 8, 50, 6, 9, 61};
        Tree tree1 = new Tree(v1, "Tree1:");
        System.out.println(tree1.toString());
        System.out.println(tree1.toString2());

       // Assignment Problem 2
        long seed = 436543;
        Random generator = new Random(seed);  // Don't use a seed if you want the numbers to be different each time
        int val = 60;
        final int SIZE = 8;

        List<Integer> v2 = new ArrayList<Integer>();
        for (int i = 0; i < SIZE * 2; i++) {
            int t = generator.nextInt(200);
            v2.add(t);
        }
        v2.add(val);
        int[] v = v2.stream().mapToInt(i -> i).toArray();
        Tree tree2 = new Tree(v, "Tree2");
        System.out.println(tree2.toString());
        tree2.contains(val);  //Sets the current node inside the tree class.
        int succCount = 5;  // how many successors do you want to see?
        System.out.println("In Tree2, starting at " + val + ENDLINE);
        for (int i = 0; i < succCount; i++) {
            System.out.println("The next successor is " + tree2.successor());
        }

        // Assignment Problem 3
        System.out.println(tree1.toString());
        System.out.println("All paths from tree1");
        tree1.printAllPaths();


        // Assignment Problem 4
        int[] v4 = {66, 75, -15, 3, 65, -83, 83, -10, 16, -7, 70, 200, 71, 90};
        Tree treeA = new Tree("TreeA");
        treeA.createTreeByLevel(v4, "TreeA");
        System.out.println(treeA.toString());
        System.out.println("treeA Contains BST: " + treeA.countBST());

        int[] a = {21, 8, 5, 6, 7, 19, 10, 40, 43, 52, 12, 60};
        Tree treeB = new Tree("TreeB");
        treeB.createTreeByLevel(a, "TreeB");
        System.out.println(treeB.toString());
        System.out.println("treeB Contains BST: " + treeB.countBST());

        // Assignment Problem 5
        treeB.pruneK(60);
        treeB.changeName("treeB after pruning 60");
        System.out.println(treeB.toString());
        treeA.pruneK(200);
        treeA.changeName("treeA after pruning 200");
        System.out.println(treeA.toString());

        // Assignment Problem 6

        System.out.println(tree1.toString());
        System.out.println("tree1 Least Common Ancestor of (56,61) " + tree1.lca(56, 61) + ENDLINE);

        System.out.println("tree1 Least Common Ancestor of (6,25) " + tree1.lca(6, 25) + ENDLINE);

        // Assignment Problem 7
        int[] v7 = {20, 15, 10, 5, 8, 2, 100, 28, 42};
        Tree tree7 = new Tree(v7, "Tree7:");

        System.out.println(tree7.toString());
        tree7.balanceTree();
        tree7.changeName("tree7 after balancing");
        System.out.println(tree7.toString());

        // Assignment Problem 8
        System.out.println(tree1.toString());
        tree1.keepRange(10, 50);
        tree1.changeName("tree1 after keeping only nodes between 10 and 50");
        System.out.println(tree1.toString());

        tree7.changeName("Tree 7");
        System.out.println(tree7.toString());
        tree7.keepRange(8, 85);
        tree7.changeName("tree7 after keeping only nodes between 8  and 85");
        System.out.println(tree7.toString());

        // Assignment Problem 9
        int[] v9 = {66, -15, -83, 3, -10, -7, 65, 16, 75, 70, 71, 83, 200, 90};
        Tree tree4 = new Tree(v9, "Tree4:");
        System.out.println(tree4.toString());
        System.out.println("maxLevelSum: " + tree4.maxLevelSum());

        // Assignment Problem 10
        ArrayList<Integer> leaves = new ArrayList<Integer>();
        int[] preorder1 = {10, 3, 1, 7, 18, 13};
        getLeaves(preorder1, 0, preorder1.length - 1, leaves);
        System.out.print("Leaves are ");
        for (int leaf : leaves) {
            System.out.print(leaf + " ");
        }
        System.out.println();

        leaves = new ArrayList<Integer>();
        int[] preorder2 = {66, -15, -83, 3, -10, -7, 65, 16, 75, 70, 71, 83, 200, 90};

        getLeaves(preorder2, 0, preorder2.length - 1, leaves);
        System.out.print("Leaves are ");
        for (int leaf : leaves) {
            System.out.print(leaf + " ");
        }
        System.out.println();

      // Assignment Problem 11
        Tree treeC = new Tree("TreeC");
        int[] data = {44, 9, 13, 4, 5, 6, 7};
        treeC.createTreeByLevel(data, "Sum Tree1 ?");
        if (treeC.isSum()) {
            System.out.println(treeC.toString() + " is Sum Tree");
        } else {
            System.out.println(treeC.toString() + " is NOT a Sum Tree");
        }
        int[] data1 = {52, 13, 13, 4, 5, 6, 7, 0, 4};
        treeC.createTreeByLevel(data1, "Sum Tree2 ?");
        if (treeC.isSum()) {
            System.out.println(treeC.toString() + " is Sum Tree");
        } else {
            System.out.println(treeC.toString() + " is NOT a Sum Tree");
        }
        int[] data2 = {44, 13, 13, 4, 5, 6, 7, 1, 4};
        treeC.createTreeByLevel(data2, "Sum Tree3?");
        if (treeC.isSum()) {
            System.out.println(treeC.toString() + " is Sum Tree");
        } else {
            System.out.println(treeC.toString() + " is NOT a Sum Tree");
        }

      // Assignment Problem 12
        treeC.changeName("Tree12");
        System.out.println(treeC.toString() + "MaxPath=" + treeC.maxPath());


        int[] data12 = {1, 3, 2, 5, 6, -3, -4, 7, 8};
        treeC.createTreeByLevel(data12, "Another Tree");
        System.out.println(treeC.toString() + "MaxPath=" + treeC.maxPath());


        //Assignment Problem 13
        int[] preorderT = {1, 2, 4, 5, 3, 6, 8, 9, 7};
        int[] postorderT = {4, 5, 2, 8, 9, 6, 7, 3, 1};
        tree1.createTreeTraversals(preorderT, postorderT, "Tree13 from preorder & postorder");
        System.out.println(tree1.toString());
        int[] preorderT2 = {5, 10, 25, 1, 57, 6, 15, 20, 3, 9, 2};
        int[] postorderT2 = {1, 57, 25, 6, 10, 20, 9, 2, 3, 15, 5};
        tree1.createTreeTraversals(preorderT2, postorderT2, "Tree from preorder & postorder");
        System.out.println(tree1.toString());
    }
}
