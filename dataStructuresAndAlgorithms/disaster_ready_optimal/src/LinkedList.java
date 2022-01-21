public class LinkedList<E> {
    private final ListNode<E> head = new ListNode<>();
    private ListNode<E> current = head;

    public void insert(E data) {
        ListNode<E> node = new ListNode(data);
        ListNode<E> current = head.next;
        ListNode<E> previous = head;

        while (current != null) {
            previous = current;
            current = current.next;
        }
        previous.next = node;
        node.next = current;
    }

    public String toString() {
        ListNode<E> current = head.next;
        StringBuilder sb = new StringBuilder();

        while (current != null) {
            sb.append(" " + current.data);
            current = current.next;
        }
        return sb.toString();
    }

    public E getNext() {
        this.current = current.next;
        return current.data;
    }

    public boolean hasNext() {
        if (current.next == null) {
            current = head;
            return false;
        }
        return true;
    }

    private static class ListNode<E> {
        public E data;
        public ListNode<E> next;

        public ListNode() {
        }

        public ListNode(E data) {
            this.data = data;
        }
    }
}