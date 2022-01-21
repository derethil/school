public class MinSkewHeap<E extends Comparable<E>> {
    private Node<E> root;
    public int sizeOfHeap = 0;

    /**
     * Inserts an element into the heap.
     *
     * @param element the item to be inserted.
     */
    public void insert(E element) {
        sizeOfHeap++;
        this.root = merge(root, new Node<E>(element));
    }

    /**
     * Deletes the minimum (root) value from the heap.
     *
     * @return E the value of the maximum element.
     */
    public E deleteMin() {
        sizeOfHeap--;
        Node<E> deletedMin = root;
        this.root = merge(root.left, root.right);
        return deletedMin.element;
    }

    /**
     * Merges two minimum skew heaps together.
     *
     * @param node1 root of the first heap
     * @param node2 root of the second heap
     * @return the root of the newly merged heap
     */
    private Node<E> merge(Node<E> node1, Node<E> node2) {
        Node<E> smaller;

        if (node1 == null) return node2;
        if (node2 == null) return node1;

        if (node1.element.compareTo(node2.element) < 0) {
            node1.right = merge(node1.right, node2);
            smaller = node1;
        } else {
            node2.right = merge(node2.right, node1);
            smaller = node2;
        }

        swapChildren(smaller);
        return smaller;
    }

    /**
     * Swaps the children of a given node.
     *
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

        Node(E element) {
            this.element = element;
        }
    }
}
