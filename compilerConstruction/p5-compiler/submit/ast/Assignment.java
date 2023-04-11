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
public class Assignment implements Expression, Node {

  private final Mutable mutable;
  private final AssignmentType type;
  private final Expression rhs;

  public Assignment(Mutable mutable, String assign, Expression rhs) {
    this.mutable = mutable;
    this.type = AssignmentType.fromString(assign);
    this.rhs = rhs;
  }

  public boolean isInitialized(SymbolTable symbolTable) {
    SymbolInfo info = symbolTable.find(mutable.getId());
    return info.isInitialized();
  }

  public void toCminus(StringBuilder builder, final String prefix) {
    mutable.toCminus(builder, prefix);
    if (rhs != null) {
      builder.append(" ").append(type.toString()).append(" ");
      rhs.toCminus(builder, prefix);
    } else {
      builder.append(type.toString());
    }
  }

  @Override
  public MIPSResult toMIPS(
          StringBuilder code,
          StringBuilder data,
          SymbolTable symbolTable,
          RegisterAllocator regAllocator
  ) {
    MIPSResult rhsMIPS = rhs.toMIPS(code, data, symbolTable, regAllocator);
    symbolTable.saveRegister(code, regAllocator, rhsMIPS.getRegister(), mutable.getId());
    regAllocator.clear(rhsMIPS.getRegister());
    return MIPSResult.createVoidResult();
  }

}
