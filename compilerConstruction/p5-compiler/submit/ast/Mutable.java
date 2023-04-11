/*
 * Code formatter project
 * CS 4481
 */
package submit.ast;

import submit.MIPSResult;
import submit.RegisterAllocator;
import submit.SymbolInfo;
import submit.SymbolTable;

/**
 *
 * @author edwajohn
 */
public class Mutable implements Expression, Node {

  private final String id;
  private final Expression index;

  public Mutable(String id, Expression index) {
    this.id = id;
    this.index = index;
  }

  public String getId() {
      return id;
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    builder.append(id);
    if (index != null) {
      builder.append("[");
      index.toCminus(builder, prefix);
      builder.append("]");
    }
  }

  @Override
  public MIPSResult toMIPS(
          StringBuilder code,
          StringBuilder data,
          SymbolTable symbolTable,
          RegisterAllocator regAllocator
  ) {
    return toMIPS(code, data, symbolTable, regAllocator, true);
  }

  public MIPSResult toMIPS(
          StringBuilder code,
          StringBuilder data,
          SymbolTable symbolTable,
          RegisterAllocator regAllocator,
          boolean shouldLoad

  ) {
    SymbolInfo symbol = symbolTable.find(id);
    if (symbol == null) {
      throw new RuntimeException("Variable " + id + " is not declared");
    }

    if (shouldLoad) {
      String register = regAllocator.getT();
      symbolTable.restoreRegister(code, regAllocator, register, id);
      return MIPSResult.createRegisterResult(register, symbol.getType());
    }

    return MIPSResult.createVoidResult();
  }
}
