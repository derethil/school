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
    MIPSResult mutableMIPS = mutable.toMIPS(code, data, symbolTable, regAllocator);

    code.append("# Compute rhs for assignment = \n");
    MIPSResult rhsMIPS = rhs.toMIPS(code, data, symbolTable, regAllocator);

    code.append("# complete assignment statement with store\n");
    String rhsRegister = rhsMIPS.getRegister();
    String mutableRegister = mutableMIPS.getRegister();

    code.append(String.format("sw %s 0(%s)", rhsRegister, mutableRegister));

    symbolTable.find(mutable.getId()).setInitialized(true);
    regAllocator.clear(mutableRegister);
    regAllocator.clear(rhsRegister);
    return MIPSResult.createVoidResult();
  }

}
