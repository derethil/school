package parser;

import java.util.*;

import static java.lang.Integer.valueOf;

public class State implements Comparable<State> {
    private Set<Item> itemSet;
    private List<Item> items;
    private int name;

    public State(int name) {
        this.itemSet = new HashSet<>();
        this.items = new ArrayList<>();
        this.name = name;
    }

    public State() {
        this(0);
    }

    public void setName(int name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        ArrayList<Item> sortedList = new ArrayList<>(items);
        sortedList.sort(Comparator.comparingInt(Item::hashCode));
        for (Item item : sortedList) {
            hash = 37 * hash + Objects.hashCode(item);
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final State other = (State) obj;
        if (items.size() != other.items.size()) {
            return false;
        }
        for (Item item : items) {
            if (!other.itemSet.contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return this.name + ": " + items.toString();
    }

    @Override
    public int compareTo(State o) {
        return Integer.compare(this.name, o.name);
    }

    public int size() {
        return items.size();
    }

    public void addItem(Item item) {
        if (!itemSet.contains(item)) {
            itemSet.add(item);
            items.add(item);
        }
    }

    public List<Item> getItems() {
        return items;
    }

}
