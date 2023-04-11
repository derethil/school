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
    String leftRegister = left.getRegister();

    MIPSResult right = rhs.toMIPS(code, data, symbolTable, regAllocator);
    String rightRegister = right.getRegister();


    switch (type) {
      case PLUS -> {
        code.append(String.format("add %s %s %s\n", leftRegister, leftRegister, rightRegister));
      }
      case MINUS -> {
        code.append(String.format("sub %s %s %s\n", leftRegister, leftRegister, rightRegister));
      }
      case TIMES -> {
        code.append(String.format("mult %s %s\n", leftRegister, rightRegister));
        code.append(String.format("mflo %s\n", leftRegister));
      }
      case DIVIDE -> {
        code.append(String.format("div %s %s\n", leftRegister, rightRegister));
        code.append(String.format("mflo %s\n", leftRegister));
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
