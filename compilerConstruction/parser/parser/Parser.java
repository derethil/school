/*
 * Look for TODO comments in this file for suggestions on how to implement
 * your parser.
 */
package parser;

import java.io.IOException;
import java.util.*;

import lexer.ExprLexer;
import lexer.ParenLexer;
import lexer.SimpleLexer;
import lexer.TinyLexer;
import org.antlr.v4.runtime.*;

/**
 *
 */
public class Parser {

  final Grammar grammar;

  /**
   * All states in the parser.
   */
  private final States states;

  /**
   * Action table for bottom-up parsing. Accessed as
   * actionTable.get(state).get(terminal). You may replace
   * the Integer with a State class if you choose.
   */
  private final HashMap<Integer, HashMap<String, Action>> actionTable;
  /**
   * Goto table for bottom-up parsing. Accessed as gotoTable.get(state).get(nonterminal).
   * You may replace the Integers with State classes if you choose.
   */
  private final HashMap<Integer, HashMap<String, Integer>> gotoTable;

  public Parser(String grammarFilename) throws IOException {
    actionTable = new HashMap<>();
    gotoTable = new HashMap<>();
    grammar = new Grammar(grammarFilename);
    states = new States();

    computeParsingTables();
  }

  public States getStates() {
    return states;
  }

  static public State computeClosure(State state, Grammar grammar) {
    State closure = new State();
    closure.setGOTO(state.getFromState(), state.getSymbol());

    for (Item I : state.getItems()) {
      State newClosure = computeClosure(I, grammar);
      for (Item item : newClosure.getItems()) {
          closure.addItem(item);
      }
    }

    return closure;
  }

  static public State computeClosure(Item I, Grammar grammar) {
    State closure = new State();
    closure.addItem(I);

    boolean hasAdded = true;
    while (hasAdded) {
      hasAdded = false;
      List<Item> itemsCopy = new ArrayList<>(closure.getItems());

      for (Item item : itemsCopy) {
        List<Rule> nextSymbolRules = grammar.rules.stream()
            .filter(rule -> rule.getLhs().equals(item.getNextSymbol()))
            .toList();

        for (Rule rule: nextSymbolRules) {
          HashSet<String> firstSet = grammar.first.get(item.getNextNextSymbol());
          if (firstSet == null) {
            firstSet = new HashSet<>();
            firstSet.add(item.getLookahead());
          }

          for (String symbol : firstSet) {
            Item newItem = new Item(rule, 0, symbol);
            if (closure.contains(newItem)) continue;
            closure.addItem(newItem);
            hasAdded = true;
          }
        }
      }
    }

    return closure;
  }

  static public State GOTO(State state, String X, Grammar grammar) {
    State gotoState = new State(state, X);
    for (Item item : state.getItems()) {
      if (item.getNextSymbol() != null && item.getNextSymbol().equals(X)) {
        gotoState.addItem(new Item(item.getRule(), item.getDot() + 1, item.getLookahead()));
      }
    }
    return computeClosure(gotoState, grammar);
  }

  public void computeStates() {
    State startState = computeClosure(new Item(grammar.startRule, 0, Util.EOF), grammar);
    states.addState(startState);

    boolean hasAdded = true;
    while (hasAdded) {
      hasAdded = false;
      List<State> statesCopy = new ArrayList<>(states.getStates());

      for (State state : statesCopy) {
        for (String symbol : grammar.symbols) {
          State gotoState = GOTO(state, symbol, grammar);
          if (gotoState.getItems().size() > 0) {
            if (states.contains(gotoState)) {
              State existingState = states.getState(gotoState);
              states.addGOTO(state, symbol, existingState);
            } else {
              states.addState(gotoState);
              states.addGOTO(state, symbol, gotoState);
              hasAdded = true;
            }
          }
        }
      }
    }
  }

  public void computeParsingTables() {
    computeStates();

    for (State state : states.getStates()) {
      addActions(state);
      addGotos(state);
    }
  }

  public void addActions(State state) {
    Integer stateName = state.getName();
    HashMap<String, Action> actionRow = new HashMap<>();

    for (Item item : state.getItems()) {
      // Condition 1

      String nextSymbol = item.getNextSymbol();
      if (nextSymbol != null) {
        if (!grammar.terminals.contains(nextSymbol)) continue;

        State gotoState = states.getGOTO(state, nextSymbol);
        if (gotoState != null) {
          Action action = Action.createShift(gotoState.getName());
          actionRow.put(nextSymbol, action);
        }
      }
      // Condition 2
      else if (item.getRule() != grammar.startRule) {
        String lookahead = item.getLookahead();
        Action action = Action.createReduce(item.getRule());
        actionRow.put(lookahead, action);
      }
      // Condition 3
      else if (item.getRule() == grammar.startRule && item.getLookahead().equals(Util.EOF)) {
        Action action = Action.createAccept();
        actionRow.put(Util.EOF, action);
      }
    }

    actionTable.put(stateName, actionRow);
  }

  public void addGotos(State state) {
    Integer stateName = state.getName();
    HashMap<String, Integer> gotoRow = new HashMap<>();

    for (String symbol : grammar.nonterminals) {
      State gotoState = states.getGOTO(state, symbol);
      if (gotoState != null) {
        gotoRow.put(symbol, gotoState.getName());
      }
    }

    gotoTable.put(stateName, gotoRow);
  }

  public String actionTableToString() {
    StringBuilder builder = new StringBuilder();

    // Header
    builder.append(String.format("%-5s", "state"));
    for (String terminal : grammar.terminals) {
      builder.append(String.format("%8s", terminal));
    }
    builder.append(String.format("%8s", Util.EOF));

    // Rows

    for (Integer state : actionTable.keySet()) {
      builder.append(String.format("%n%-5s", state));
      HashMap<String, Action> actionRow = actionTable.get(state);

      // Terminals
      for (String terminal : grammar.terminals) {
        Action action = actionRow.get(terminal);
        if (action == null) {
          builder.append(String.format("%8s", ""));
        } else {
          builder.append(String.format("%8s", action));
        }
      }
      // EOF
      Action action = actionRow.get(Util.EOF);
      if (action == null) {
        builder.append(String.format("%10s", ""));
      } else {
        builder.append(String.format("%10s", action));
      }
    }


    return builder.toString();
  }

  public String gotoTableToString() {
    StringBuilder builder = new StringBuilder();

    // Header
    builder.append(String.format("%-5s", "state"));
    for (String nonterminal : grammar.nonterminals) {
      builder.append(String.format("%8s", nonterminal));
    }

    // Rows

    for (Integer state : gotoTable.keySet()) {
      builder.append(String.format("%n%-5s", state));
      HashMap<String, Integer> gotoRow = gotoTable.get(state);

      // Nonterminals
      for (String nonterminal : grammar.nonterminals) {
        Integer gotoState = gotoRow.get(nonterminal);
        if (gotoState == null) {
          builder.append(String.format("%8s", ""));
        } else {
          builder.append(String.format("%8s", gotoState));
        }
      }
    }

    return builder.toString();
  }

  // TODO: Implement this method
  // You should return a list of the actions taken.
  public List<Action> parse(Lexer scanner) throws ParserException {
    // tokens is the output from the scanner. It is the list of tokens
    // scanned from the input file.
    // To get the token type: v.getSymbolicName(t.getType())
    // To get the token lexeme: t.getText()
    ArrayList<? extends Token> tokens = new ArrayList<>(scanner.getAllTokens());
    Vocabulary v = scanner.getVocabulary();

    Stack<String> input = new Stack<>();
    Collections.reverse(tokens);
    input.add(Util.EOF);
    for (Token t : tokens) {
      input.push(v.getSymbolicName(t.getType()));
    }
    Collections.reverse(tokens);
//    System.out.println(input);

    // TODO: Parse the tokens. On an error, throw a ParseException, like so:
    //    throw ParserException.create(tokens, i)
    List<Action> actions = new ArrayList<>();

    String a = input.pop();
    Stack<Integer> stateStack = new Stack<>();
//    Stack<String> symbolStack = new Stack<>();
    stateStack.push(0);

    while (true) {
      Integer s = stateStack.peek();
      Action action = actionTable.get(s).get(a);

      if (action == null) {
        throw ParserException.create(tokens, tokens.size() - input.size());
      }

      if (action.isShift()) {
        // Push t onto the stack
        Integer t = action.getState();
        stateStack.push(t);
        // Let a be the next input symbol
        a = input.pop();
        // Add the action to the list of actions
        actions.add(action);
      }
      else if (action.isReduce()) {
        Rule rule = action.getRule();
        // Pop size of beta off the stack
        for (int i = 0; i < rule.getRhs().size(); i++) {
          stateStack.pop();
        }
        // Push GOTO[t, A] onto the stack
        Integer t = stateStack.peek();
        stateStack.push(gotoTable.get(t).get(rule.getLhs()));
        // Add the rule to the list of actions
        actions.add(action);
      }
      else if (action.isAccept()) {
        // Add the action to the list of actions
        actions.add(action);
        break;
      }
      else {
        throw ParserException.create(tokens, tokens.size() - input.size());
      }
    }

    return actions;
  }

  //-------------------------------------------------------------------
  // Convenience functions
  //-------------------------------------------------------------------

  public List<Action> parseFromFile(String filename) throws IOException, ParserException {
//    System.out.println("\nReading input file " + filename + "\n");
    final CharStream charStream = CharStreams.fromFileName(filename);
    Lexer scanner = scanFile(charStream);
    return parse(scanner);
  }

  public List<Action> parseFromString(String program) throws ParserException {
    Lexer scanner = scanFile(CharStreams.fromString(program));
    return parse(scanner);
  }

  private Lexer scanFile(CharStream charStream) {
    // We use ANTLR's scanner (lexer) to produce the tokens.
    Lexer scanner = null;
    switch (grammar.grammarName) {
      case "Simple":
        scanner = new SimpleLexer(charStream);
        break;
      case "Paren":
        scanner = new ParenLexer(charStream);
        break;
      case "Expr":
        scanner = new ExprLexer(charStream);
        break;
      case "Tiny":
        scanner = new TinyLexer(charStream);
        break;
      default:
        System.out.println("Unknown scanner");
        break;
    }

    return scanner;
  }

}
