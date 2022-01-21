package com.example.webbrowser;

public class HistoryList {
    class Node{
        String url;
        Node previous;
        Node next;

        public Node(String url) {
            this.url = url;
        }
    }
    Node current = null;

    public void addUrl(String url) {
        Node newNode = new Node(url);

        if(current == null) {
            current = newNode;
            current.previous = null;
        }
        else {
            current.next = newNode;
            newNode.previous = current;
            current = newNode;
            current.next = null;
        }
    }

    public String getPreviousUrl() {
        if (current == null || current.previous == null) { return ""; }
        current = current.previous;
        return current.url;
    }

    public String getNextUrl() {
        if (current == null || current.next == null) { return ""; }
        current = current.next;
        return current.url;
    }
    public boolean hasPreviousUrl() { return current != null && current.previous != null; }

    public boolean hasNextUrl() { return current != null && current.next != null; }
}
