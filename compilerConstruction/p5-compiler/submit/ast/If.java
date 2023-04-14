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
public class If implements Statement {

  private final Expression expression;
  private final Statement trueStatement;
  private final Statement falseStatement;

  public If(Expression expression, Statement trueStatement, Statement falseStatement) {
    this.expression = expression;
    this.trueStatement = trueStatement;
    this.falseStatement = falseStatement;
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    builder.append(prefix).append("if (");
    expression.toCminus(builder, prefix);
    builder.append(")\n");
    if (trueStatement instanceof CompoundStatement) {
      trueStatement.toCminus(builder, prefix);
    } else {
      trueStatement.toCminus(builder, prefix + " ");
    }
    if (falseStatement != null) {
      builder.append(prefix).append("else\n");
//      falseStatement.toCminus(builder, prefix);
      if (falseStatement instanceof CompoundStatement) {
        falseStatement.toCminus(builder, prefix);
      } else {
        falseStatement.toCminus(builder, prefix + " ");
      }
    }
//    builder.append(prefix).append("}");
  }

  @Override
  public MIPSResult toMIPS(
          StringBuilder code,
          StringBuilder data,
          SymbolTable symbolTable,
          RegisterAllocator regAllocator
  ) {
    MIPSResult expressionResult = expression.toMIPS(code, data, symbolTable, regAllocator);
    String expressionRegister = expressionResult.getRegister();
    String falseLabel = symbolTable.getUniqueLabel("falseLabel");
    String trueLabel = symbolTable.getUniqueLabel("trueLabel");

    code.append(String.format("bne %s $zero %s\n", expressionRegister, falseLabel));
    regAllocator.clear(expressionRegister);

    trueStatement.toMIPS(code, data, symbolTable, regAllocator);
    code.append(String.format("j %s\n", trueLabel));

    code.append(String.format("%s:\n", falseLabel));

    if (falseStatement != null) falseStatement.toMIPS(code, data, symbolTable, regAllocator);
    code.append(String.format("%s:\n", trueLabel));

    return null;
  }
}
