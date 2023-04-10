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
public class BinaryOperator implements Expression {

  private final Expression lhs, rhs;
  private final BinaryOperatorType type;

  public BinaryOperator(Expression lhs, BinaryOperatorType type, Expression rhs) {
    this.lhs = lhs;
    this.type = type;
    this.rhs = rhs;
  }

  public BinaryOperator(Expression lhs, String type, Expression rhs) {
    this.lhs = lhs;
    this.type = BinaryOperatorType.fromString(type);
    this.rhs = rhs;
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    lhs.toCminus(builder, prefix);
    builder.append(" ").append(type).append(" ");
    rhs.toCminus(builder, prefix);
  }

  @Override
  public MIPSResult toMIPS(
          StringBuilder code,
          StringBuilder data,
          SymbolTable symbolTable,
          RegisterAllocator regAllocator
  ) {
    MIPSResult left = lhs.toMIPS(code, data, symbolTable, regAllocator);
    MIPSResult right = rhs.toMIPS(code, data, symbolTable, regAllocator);

    String leftRegister = left.getRegister();
    String rightRegister = right.getRegister();

    switch (type) {
      case PLUS -> {
        code.append("add ").append(leftRegister).append(" ").append(leftRegister).append(" ").append(rightRegister).append("\n");
      }
      case MINUS -> {
          code.append("sub ").append(leftRegister).append(" ").append(leftRegister).append(" ").append(rightRegister).append("\n");
      }
      case TIMES -> {
        code.append("mult ").append(leftRegister).append(" ").append(rightRegister).append("\n");
        code.append("mflo ").append(leftRegister).append("\n");
      }
      case DIVIDE -> {
        code.append("div ").append(leftRegister).append(" ").append(rightRegister).append("\n");
        code.append("mflo ").append(leftRegister).append("\n");
      }
      default -> {
        throw new UnsupportedOperationException("BinaryOperatorType " + type + " not supported yet.");
      }
    }

    regAllocator.clear(rightRegister);

    switch (type) {
      case PLUS, MINUS, TIMES, DIVIDE -> {
        return MIPSResult.createRegisterResult(leftRegister, VarType.INT);
      }
      default -> {
        return MIPSResult.createVoidResult();
      }
    }
  }
}
