public class MaxLeftistHeap<E extends Comparable<E>> {
    private Node<E> root;
    public int sizeOfHeap = 0;

    /**
     * Inserts an element into the heap.
     * @param element the item to be inserted.
     */
    public void insert(E element) {
        sizeOfHeap++;
        this.root = merge(root, new Node<E>(element));
    }

    /**
     * Deletes the maximum (root) value from the heap.
     * @return E the value of the maximum element.
     */
    public E deleteMax() {
        sizeOfHeap--;
        Node<E> deletedMax = root;
        this.root = merge(root.left, root.right);
        return deletedMax.element;
    }

    /**
     * Gets the null path length of a node.
     * This allows you to get the NPL of a null node.
     * @param node the node to get the npl from.
     * @return the npl of the node
     */
    private int getNpl(Node<E> node) {
        if (node == null) return -1;
        return node.npl;
    }

    /**
     * Merges two max leftist heaps together.
     * @param node1 root of the first heap
     * @param node2 root of the second heap
     * @return the root of the newly merged heap
     */
    private Node<E> merge(Node<E> node1, Node<E> node2) {
        Node<E> big;

        if (node1 == null) return node2;
        if (node2 == null) return node1;

        if (node1.element.compareTo(node2.element) > 0) {
            node1.right = merge(node1.right, node2);
            big = node1;
        } else {
            node2.right = merge(node2.right, node1);
            big = node2;
        }

        if (getNpl(big.left) < getNpl(big.right)) swapChildren(big);
        setNpl(big);
        return big;
    }

    /**
     * Sets the npl on a node.
     * @param node the node to set the npl for
     */
    private void setNpl(Node<E> node) {
        node.npl = Math.min(getNpl(node.left), getNpl(node.right)) + 1;
    }

    /**
     * Swaps the children of a given node.
     * @param node the node to swap the children on.
     */
    private void swapChildren(Node<E> node) {
        Node<E> tempLeft = node.left;
        node.left = node.right;
        node.right = tempLeft;
    }

    private static class Node<E> {
        E element;
        Node<E> left;
        Node<E> right;
        int npl;

        Node (E element) { this.element = element; }
    }
}
