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
public class While implements Statement {

  private final Expression expression;
  private final Statement statement;

  public While(Expression expression, Statement statement) {
    this.expression = expression;
    this.statement = statement;
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    builder.append(prefix).append("while (");
    expression.toCminus(builder, prefix);
    builder.append(")\n");
    if (statement instanceof CompoundStatement) {
      statement.toCminus(builder, prefix);
    } else {
      statement.toCminus(builder, prefix + " ");
    }
  }

  @Override
  public MIPSResult toMIPS(
          StringBuilder code,
          StringBuilder data,
          SymbolTable symbolTable,
          RegisterAllocator regAllocator
  ) {
    String loopLabel = symbolTable.getUniqueLabel("loopLabel");
    String endLabel = symbolTable.getUniqueLabel("endLabel");

    code.append(String.format("%s:\n", loopLabel));
    MIPSResult expressionResult = expression.toMIPS(code, data, symbolTable, regAllocator);
    String expressionRegister = expressionResult.getRegister();

    code.append(String.format("bne %s $zero %s\n", expressionRegister, endLabel));
    regAllocator.clear(expressionRegister);

    MIPSResult statementResult = statement.toMIPS(code, data, symbolTable, regAllocator);
    regAllocator.clear(statementResult.getRegister());

    code.append(String.format("j %s\n", loopLabel));
    code.append(String.format("%s:\n", endLabel));

    return MIPSResult.createVoidResult();
  }
}
