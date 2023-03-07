package parser;

import java.util.*;

public class States {
    private Set<State> stateSet;
    private List<State> states;

    private HashMap<Integer, HashMap<String, Integer>> gotoSet;
    private int id = 0;

    public States() {
        this.gotoSet = new HashMap<>();
        this.stateSet = new HashSet<>();
        this.states = new ArrayList<>();
    }

    public State getState(int name) {
        if (name >= states.size()) {
            return null;
        }
        return states.get(name);
    }

    @Override
    public String toString() {
        return states.toString();
    }

    public int size() {
        return states.size();
    }

    public void addState(State state) {
        if (stateSet.contains(state)) return;
        state.setName(id++);
        stateSet.add(state);
        states.add(state);
    }

    public State getState(State state) {
        for (State s : stateSet) {
            if (s.equals(state)) return s;
        }
        return null;
    }

    public List<State> getStates() {
        return states;
    }

    public boolean contains(State state) {
        return stateSet.contains(state);
    }

    public State getGOTO(State fromState, String symbol) {
        Integer fromStateName = fromState.getName();
        if (gotoSet.containsKey(fromStateName) && gotoSet.get(fromStateName).containsKey(symbol)) {
            return getState(gotoSet.get(fromStateName).get(symbol));
        }
        return null;
    }

    public void addGOTO(State fromState, String symbol, State toState) {
        gotoSet.putIfAbsent(fromState.getName(), new HashMap<>());
        gotoSet.get(fromState.getName()).put(symbol, toState.getName());
    }
}
