package scanner;

import java.util.HashMap;
import java.util.Stack;

/**
 * This is the file you will modify.
 */
public class Scanner {
    private final HashMap<Character, String> categoryTable = new HashMap<Character, String>();
    private final HashMap<String, HashMap<String, String>> transitionTable = new HashMap<String, HashMap<String, String>>();
    private final HashMap<String, String> tokenTypeTable = new HashMap<String, String>();

  /**
   * Builds the tables needed for the scanner.
   */
  public Scanner(TableReader tableReader) {
    // Build categoryTable, mapping a character to a category.
    for (TableReader.CharCat cat : tableReader.getClassifier()) {
      categoryTable.put(cat.getC(), cat.getCategory());
    }

    // Build the transition table. Given a state and a character category,
    // give a new state.
    for (TableReader.Transition t : tableReader.getTransitions()) {
      HashMap<String, String> toStates = transitionTable.get(t.getFromStateName());
      if (toStates == null) toStates = new HashMap<String, String>();

      toStates.put(t.getCategory(), t.getToStateName());
      transitionTable.put(t.getFromStateName(), toStates);
    }

    // Build the token types table
    for (TableReader.TokenType tt : tableReader.getTokens()) {
      tokenTypeTable.put(tt.getState(), tt.getType());
    }

  }

  /**
   * Returns the category for c or "not in alphabet" if c has no category.
   */
  public String getCategory(Character c) {
    String category = this.categoryTable.get(c);
    if (category == null) {
      return "not in alphabet";
    }
    return category;
  }

  /**
   * Returns the new state given a current state and category.
   */
  public String getNewState(String state, String category) {
    HashMap<String, String> toStates = this.transitionTable.get(state);
    if (toStates == null) return "error";

    String newState = toStates.get(category);
    if (newState == null) return "error";

    return newState;
  }

  /**
   * Returns the type of token corresponding to a given state. If the state
   * is not accepting then return "error".
   * Do not hardcode any state names or token types.
   */
  public String getTokenType(String state) {
    String tokenType = this.tokenTypeTable.get(state);
    if (tokenType == null) return "error";
    return tokenType;
  }

  /**
   * Returns true if the state is accepting.
   */
  private boolean isAccepting(String state) {
    return this.tokenTypeTable.containsKey(state);
  }

  /**
   * Return the next token or null if there's a lexical error.
   */
  public Token nextToken(ScanStream ss) {
    Stack<String> stack = new Stack<>();
    String state = "s0";
    StringBuilder lexeme = new StringBuilder();

    stack.push("bad");

    while (!state.equals("error") && !ss.eof()) {
      char c = ss.next();
      lexeme.append(c);

      if (isAccepting(state)) stack.clear();

      stack.push(state);
      String category = getCategory(c);
      state = getNewState(state, category);
    }

    while (!isAccepting(state) && !state.equals("bad")) {
      state = stack.pop();
      if (lexeme.length() > 0) lexeme.deleteCharAt(lexeme.length() - 1);
      ss.rollback();
    }

    if (isAccepting(state)) {
      String tokenType = getTokenType(state);
      return new Token(tokenType, lexeme.toString());
    }

    return null;
  }

}
