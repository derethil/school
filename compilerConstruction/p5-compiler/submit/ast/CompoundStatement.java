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

  public CompoundStatement(List<Statement> statements) {
    this.statements = statements;
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

    SymbolTable child = symbolTable.getChild(0);
    code.append("# Symbols in symbol table:\n");

    List<String> symbols = child.getSymbols();
    symbols.forEach((symbol) -> {
      code.append("#  ").append(symbol).append("\n");
    });

    code.append("# Update the stack pointer.\n");
    code.append("addi $sp $sp -0\n"); // TODO: actually compute the size of the stack frame

    statements.forEach((statement) -> {
      statement.toMIPS(code, data, symbolTable, regAllocator);
    });

    code.append("# Exiting scope.\n");
    code.append("addi $sp $sp 0\n"); // TODO: actually compute the size of the stack frame

    return MIPSResult.createVoidResult();
  }

}
