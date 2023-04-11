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
    SymbolInfo symbol = symbolTable.find(id);
    if (symbol == null) {
      throw new RuntimeException("Variable " + id + " is not declared");
    }

    String register = regAllocator.getT();

    // assume no arrays for now
    code.append(String.format("# Get %s's offset from $sp from the symbol table and initialize %s's address with it. We'll add $sp later.\n", id, id));
    code.append(String.format("li %s -%d\n", register, symbol.getOffset()));

    code.append("# Add the stack pointer address to the offset.\n");
    code.append(String.format("add %s %s $sp\n", register, register));


    if (symbol.isInitialized()) {
      String toRegister = regAllocator.getT();
      code.append(String.format("# Load the value of %s.\n", id));
      code.append(String.format("lw %s 0(%s)\n", toRegister, register));
      regAllocator.clear(register);
      return MIPSResult.createRegisterResult(toRegister, symbol.getType());
    }

    return MIPSResult.createRegisterResult(register, symbol.getType());
  }
}
