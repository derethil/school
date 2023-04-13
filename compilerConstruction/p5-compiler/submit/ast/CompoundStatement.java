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
      code.append(String.format("#  %s \n", symbol));
    });

    int parentTableSize = this.symbolTable.getParent().getSize();

    code.append("# Update the stack pointer.\n");
    code.append(String.format("addi $sp $sp %d\n", parentTableSize));

    statements.forEach((statement) -> {
      statement.toMIPS(code, data, this.symbolTable, regAllocator);
      code.append("\n");
    });

    code.append("# Exiting scope.\n");
    code.append(String.format("addi $sp $sp %d\n", -parentTableSize));

    return MIPSResult.createVoidResult();
  }

  public SymbolTable getSymbolTable() {
    return symbolTable;
  }
}
