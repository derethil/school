package submit;

import submit.ast.VarType;

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

  private int size;

  public SymbolTable() {
    size = 0;
    table = new HashMap<>();
    parent = null;
    children = new ArrayList<>();

    // Add the built-in functions to the symbol table.
    addSymbol("println", new SymbolInfo("println", null, true));
  }

  public void addSymbol(String id, SymbolInfo symbol) {
    table.put(id, symbol);
    if (symbol.getType() != null) {
      int typeSize = symbol.getType().getSize();
      size -= symbol.getType().getSize();
      symbol.setOffset(size);
    }
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

  public SymbolInfo findWithOffset(String id) {
    return findWithOffset(id, 0);
  }

  public SymbolInfo findWithOffset(String id, int totalParentOffset) {
    if (table.containsKey(id)) {
      SymbolInfo symbol = table.get(id).copy();
      symbol.setOffset(totalParentOffset + symbol.getOffset());
      return symbol;
    }
    if (parent != null) {
      return parent.findWithOffset(id, totalParentOffset - parent.size);
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

  public void saveRegister(StringBuilder code, RegisterAllocator regAllocator, String register, String id) {
    SymbolInfo symbol = findWithOffset(id);
    if (symbol == null) {
      throw new RuntimeException("Symbol not found: " + id);
    }
    regAllocator.saveOneT(code, register, symbol.getOffset());
  }

  public void restoreRegister(StringBuilder code, RegisterAllocator regAllocator, String register, String id) {
    SymbolInfo symbol = findWithOffset(id);
    if (symbol == null) {
      throw new RuntimeException("Symbol not found: " + id);
    }
    int offset = symbol.getOffset();
    regAllocator.restoreOneT(code, register, symbol.getOffset());
  }

  public int getSize() {
    return size;
  }

  public int getTotalSize() {
    if (parent == null) {
      return size;
    }
    return size + parent.getTotalSize();
  }
}
