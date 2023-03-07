package parser;

import java.util.*;

import static java.lang.Integer.valueOf;

public class State implements Comparable<State> {
    private Set<Item> itemSet;
    private List<Item> items;
    private int name;

    private State fromState;
    private String symbol;

    public State(int name) {
        this.itemSet = new HashSet<>();
        this.items = new ArrayList<>();
        this.name = name;
    }

    public State(int name, State fromState, String symbol) {
        this(name);
        this.fromState = fromState;
        this.symbol = symbol;
    }

    public State(State fromState, String symbol) {
        this.itemSet = new HashSet<>();
        this.items = new ArrayList<>();
        this.fromState = fromState;
        this.symbol = symbol;
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

    public void setGOTO(State fromState, String symbol) {
        this.fromState = fromState;
        this.symbol = symbol;
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

    public boolean addItem(Item item) {
        if (!itemSet.contains(item)) {
            itemSet.add(item);
            items.add(item);
            return true;
        }
        return false;
    }

    public List<Item> getItems() {
        return items;
    }

    public boolean contains(Item item) {
        return itemSet.contains(item);
    }

    public State getFromState() {
        return fromState;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getName() {
        return name;
    }

    public boolean isThisGOTO(State fromState, String symbol) {
        if (this.fromState == null || this.symbol == null) return false;
        return this.fromState.equals(fromState) && this.symbol.equals(symbol);
    }

}
