package submit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Code formatter project
 * CS 4481
 */
/**
 *
 */
public class SymbolTable {
  private static int labelCount = 0;

  private final HashMap<String, SymbolInfo> table;
  private SymbolTable parent;
  private final List<SymbolTable> children;

  public SymbolTable() {
    table = new HashMap<>();
    parent = null;
    children = new ArrayList<>();

    // Add the built-in functions to the symbol table.
    addSymbol("println", new SymbolInfo("println", null, true));
  }

  public void addSymbol(String id, SymbolInfo symbol) {
    table.put(id, symbol);
  }

  /**
   * Returns null if no symbol with that id is in this symbol table or an
   * ancestor table.
   *
   * @param id
   * @return
   */
  public SymbolInfo find(String id) {
    if (table.containsKey(id)) {
      return table.get(id);
    }
    if (parent != null) {
      return parent.find(id);
    }
    return null;
  }

  /**
   * Returns the new child.
   *
   * @return
   */
  public SymbolTable createChild() {
    SymbolTable child = new SymbolTable();
    children.add(child);
    child.parent = this;
    return child;
  }

  public SymbolTable getChild(int index) {
    return children.get(index);
  }

  public SymbolTable getParent() {
    return parent;
  }

  public String getUniqueLabel() {
    return "datalabel" + labelCount++;
  }

  public List<String> getSymbols() {
    return new ArrayList<>(table.keySet());
  }

  // a function to find the child of a symbol table
    public SymbolTable findChild(String id) {
        for (SymbolTable child : children) {
            if (child.find(id) != null) {
                return child;
            }
        }
        return null;
    }
}
