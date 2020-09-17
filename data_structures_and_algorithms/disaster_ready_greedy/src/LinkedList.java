import java.util.HashSet;
import java.util.Set;

public class LinkedList {
    private Node head;
    public int counter;
    public LinkedList() {

    }

    public void insert(Object data) {
        if (head == null) {
            head = new Node(data);
        }

        Node temp = new Node(data);
        Node current = head;

        if (current != null) {
            while (current.getNext() != null) {
                current = current.getNext();
            }

            current.setNext(temp);
        }

        counter++;
    }



    public String toString() {
        Node current = head.next;
        StringBuilder sb = new StringBuilder();

        while (current != null) {
            sb.append(" " + current.data);
            current = current.next;
        }

        return sb.toString();
    }

    public Object getNext() {
        this.current = current.next;
        return current.data;
    }

    public boolean hasNext() {
        if (null == current) {
            current = head;
            return  false;
        }
        return true;
    }

    public void resetNext() {
        current = head;
    }

    public void removeDuplicates() {
        Node previous = null;
        Node start = head;

        Set set = new HashSet<>();

        while (current != null) {
            if (set.contains(current.data)) {
                previous.next = current.next;
            } else {
                set.add(current.data);
                previous = current;
            }
            current = previous.next;
        }
        head = start;
    }

    private class Node {
        public Object data;
        public Node next;

        public Node(Object dataValue) {
            next = null;
            data = dataValue;
        }

        public Object getData() {
            return data;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node nextValue) {
            next = nextValue;
        }
    }

}