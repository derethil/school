import com.sun.source.tree.Tree;

public class BinarySearchTree<E extends Comparable<E>> {
    private TreeNode<E> root;

    public boolean insert(E value) {
        if (search(value)) { return false; }
        if (root == null) {
            this.root = new TreeNode<>(value);
        }
        else {
            TreeNode<E> parent = null;
            TreeNode<E> node = root;

            while (node != null) {
                parent = node;
                if (node.data.compareTo(value) < 0) {
                    node = node.right;
                } else {
                    node = node.left;
                }
            }

            TreeNode<E> nodeNew = new TreeNode<>(value);
            if (parent.data.compareTo(value) < 0) {
                parent.right = nodeNew;
            } else {
                parent.left = nodeNew;
            }
        }
        return true;
    }

    public boolean remove (E value) {
        if (!search(value)) { return false; }
        TreeNode<E> parent = null;
        TreeNode<E> node = root;
        boolean done = false;
        while (!done) {
            if (node.data.compareTo(value) < 0) {
                parent = node;
                node = node.right;
            } else if (node.data.compareTo(value) > 0) {
                parent = node;
                node = node.left;
            } else {
                done = true;
            }
        }

        if (node.left == null) {
            if (parent == null) {
                root = node.right;
            } else {
                if (parent.data.compareTo(value) < 0) {
                    parent.right = node.right;
                } else {
                    parent.left = node.right;
                }
            }
        } else {
            TreeNode<E> parentOfRight = node;
            TreeNode<E> rightMost = node.left;

            while (rightMost.right != null) {
                parentOfRight = rightMost;
                rightMost = rightMost.right;
            }
            node.data = rightMost.data;
            if (parentOfRight.right == rightMost) {
                parentOfRight.right = rightMost.left;
            } else {
                parentOfRight.left = rightMost.left;
            }
        }
        return true;
    }

    public boolean search (E value) {
        TreeNode<E> node = root;
        while (node != null) {
            if (node.data.compareTo(value) == 0) {
                return true;
            }
            if (node.data.compareTo(value) < 0) {
                node = node.right;
            } else {
                node = node.left;
            }
        }

        return false;
    }

    public void display(String message) {
        System.out.println(message);
        traverse(root);
    }

    private void traverse( TreeNode<E> node) {
        if (node == null) return;

        traverse(node.left);
        System.out.println(node.data);
        traverse(node.right);
    }

    public int numberNodes() {
        return numberNodes(this.root);
    }

    private int numberNodes(TreeNode<E> node) {
        if (node == null) { return 0; }
        return 1 + numberNodes(node.left) + numberNodes(node.right);
    }

    public int numberLeafNodes() {
        return numberLeafNodes(root);
    }

    private int numberLeafNodes(TreeNode<E> node) {
        if (node == null) { return 0; }
        if (node.left == null && node.right == null) { return 1; }
        return numberLeafNodes(node.left) + numberLeafNodes(node.right);

    }

    public int height() { return height(this.root); }

    private int height(TreeNode<E> node) {
        if (node == null) { return -1; }
        if (node.left == null && node.right == null) { return 0; }
        return Math.max(height(node.left), height(node.right)) + 1;
    }

    private static class TreeNode<E> {
        public E data;
        public TreeNode<E> left;
        public TreeNode<E> right;

        public TreeNode(E data) {
            this.data = data;
        }
    }
}
