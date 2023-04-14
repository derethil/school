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
public class UnaryOperator implements Expression {

  private final UnaryOperatorType type;
  private final Expression expression;

  public UnaryOperator(String type, Expression expression) {
    this.type = UnaryOperatorType.fromString(type);
    this.expression = expression;
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    builder.append(type);
    expression.toCminus(builder, prefix);
  }

  @Override
  public MIPSResult toMIPS(
          StringBuilder code,
          StringBuilder data,
          SymbolTable symbolTable,
          RegisterAllocator regAllocator
  ) {
    MIPSResult result = expression.toMIPS(code, data, symbolTable, regAllocator);
    String register = result.getRegister();

    switch (type) {
      case NEG -> {
        code.append(String.format("neg %s %s\n", register, register));
      }
      default -> {
        throw new UnsupportedOperationException(String.format("Operator %s supported yet.", type));
      }
    }
    return MIPSResult.createRegisterResult(register, VarType.INT);
  }


}
