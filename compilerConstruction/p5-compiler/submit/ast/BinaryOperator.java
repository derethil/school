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
      case MOD -> {
        code.append(String.format("div %s %s\n", leftRegister, rightRegister));
        code.append(String.format("mfhi %s\n", leftRegister));
      }
      case LT -> {
        code.append(String.format("slt %s %s %s\n", leftRegister, leftRegister, rightRegister));
        code.append(String.format("xori %s %s 1\n", leftRegister, leftRegister));
      }
      case GT -> {
        code.append(String.format("sgt %s %s %s\n", leftRegister, leftRegister, rightRegister));
        code.append(String.format("xori %s %s 1\n", leftRegister, leftRegister));
      }
      case LE -> {
        code.append(String.format("sle %s %s %s\n", leftRegister, rightRegister, leftRegister));
        code.append(String.format("xori %s %s 1\n", leftRegister, leftRegister));
      }
      case GE -> {
        code.append(String.format("sge %s %s %s\n", leftRegister, leftRegister, rightRegister));
        code.append(String.format("xori %s %s 1\n", leftRegister, leftRegister));
      }
      case EQ -> {
        code.append(String.format("seq %s %s %s\n", leftRegister, leftRegister, rightRegister));
        code.append(String.format("xori %s %s 1\n", leftRegister, leftRegister));
      }
      case NE -> {
        code.append(String.format("sne %s %s %s\n", leftRegister, leftRegister, rightRegister));
        code.append(String.format("xori %s %s 1\n", leftRegister, leftRegister));
      }
      case AND -> {
        code.append(String.format("xori %s %s 1\n", leftRegister, leftRegister));
        code.append(String.format("xori %s %s 1\n", rightRegister, rightRegister));
        code.append(String.format("and %s %s %s\n", leftRegister, leftRegister, rightRegister));
        code.append(String.format("xori %s %s 1\n", leftRegister, leftRegister));
      }
      case OR -> {
        code.append(String.format("xori %s %s 1\n", leftRegister, leftRegister));
        code.append(String.format("xori %s %s 1\n", rightRegister, rightRegister));
        code.append(String.format("or %s %s %s\n", leftRegister, leftRegister, rightRegister));
        code.append(String.format("xori %s %s 1\n", leftRegister, leftRegister));
      }
      default -> {
        throw new UnsupportedOperationException("BinaryOperatorType " + type + " not supported yet.");
      }
    }

    regAllocator.clear(rightRegister);
    return MIPSResult.createRegisterResult(leftRegister, VarType.INT);

  }
}
