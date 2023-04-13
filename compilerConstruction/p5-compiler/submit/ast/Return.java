/*
 * Code formatter project
 * CS 4481
 */
package submit.ast;

import submit.MIPSResult;
import submit.RegisterAllocator;
import submit.SymbolTable;

/**
 *
 * @author edwajohn
 */
public class Return implements Statement {

  private final Expression expr;

  public Return(Expression expr) {
    this.expr = expr;
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    builder.append(prefix);
    if (expr == null) {
      builder.append("return;\n");
    } else {
      builder.append("return ");
      expr.toCminus(builder, prefix);
      builder.append(";\n");
    }
  }

  @Override
  public MIPSResult toMIPS(
          StringBuilder code,
          StringBuilder data,
          SymbolTable symbolTable,
          RegisterAllocator regAllocator
  ) {
    if (expr != null) {
      MIPSResult result = expr.toMIPS(code, data, symbolTable, regAllocator);
      symbolTable.saveRegister(code, regAllocator, result.getRegister(), "return");
      regAllocator.clear(result.getRegister());
      return MIPSResult.createVoidResult();
    }
    return MIPSResult.createVoidResult();
  }

}
