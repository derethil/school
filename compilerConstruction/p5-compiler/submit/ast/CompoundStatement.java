/*
 * Code formatter project
 * CS 4481
 */
package submit.ast;

import submit.MIPSResult;
import submit.RegisterAllocator;
import submit.SymbolTable;

import java.util.List;

/**
 *
 * @author edwajohn
 */
public class CompoundStatement implements Statement {

  private final List<Statement> statements;

  private final SymbolTable symbolTable;

  public CompoundStatement(List<Statement> statements, SymbolTable symbolTable) {
    this.statements = statements;
    this.symbolTable = symbolTable;
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    builder.append(prefix).append("{\n");
    for (Statement s : statements) {
      s.toCminus(builder, prefix + "  ");
    }
    builder.append(prefix).append("}\n");
  }

  @Override
  public MIPSResult toMIPS(
          StringBuilder code,
          StringBuilder data,
          SymbolTable symbolTable,
          RegisterAllocator regAllocator
  ) {
    code.append("# Entering a new scope.\n");
    code.append("# Symbols in symbol table:\n");
    this.symbolTable.getSymbols().forEach((symbol) -> {
      code.append(String.format("# %s \n", symbol));
    });

    code.append("# Update the stack pointer.\n");
    code.append("addi $sp $sp -0\n"); // TODO: actually compute the size of the stack frame

    statements.forEach((statement) -> {
      statement.toMIPS(code, data, this.symbolTable, regAllocator);
      code.append("\n");
    });

    code.append("# Exiting scope.\n");
    code.append("addi $sp $sp 0\n"); // TODO: actually compute the size of the stack frame

    return MIPSResult.createVoidResult();
  }

}
