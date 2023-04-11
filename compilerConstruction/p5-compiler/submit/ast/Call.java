/*
 * Code formatter project
 * CS 4481
 */
package submit.ast;

import submit.MIPSResult;
import submit.RegisterAllocator;
import submit.SymbolTable;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author edwajohn
 */
public class Call implements Expression {

  private final String id;
  private final List<Expression> args;

  public Call(String id, List<Expression> args) {
    this.id = id;
    this.args = new ArrayList<>(args);
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    builder.append(id).append("(");
    for (Expression arg : args) {
      arg.toCminus(builder, prefix);
      builder.append(", ");
    }
    if (!args.isEmpty()) {
      builder.setLength(builder.length() - 2);
    }
    builder.append(")");
  }

  @Override
  public MIPSResult toMIPS(
          StringBuilder code,
          StringBuilder data,
          SymbolTable symbolTable,
          RegisterAllocator regAllocator
  ) {
    code.append(String.format("# Calling function %s\n", id));
    switch (id) {
      case "println" -> {
        MIPSResult result = args.get(0).toMIPS(code, data, symbolTable, regAllocator);
        VarType type = result.getType();

        switch (type) {
          case INT -> {
            String register = result.getRegister();
            code.append(String.format("move $a0 %s\n", register));
            code.append("li $v0 1\n");
            regAllocator.clear(register);
          }
          case CHAR -> {
            code.append(String.format("la $a0 %s\n", result.getAddress()));
            code.append("li $v0 4\n");
          }
          default -> {
            return null;
          }
        }

        code.append("syscall\n");
        code.append("la $a0 newline\n");
        code.append("li $v0 4\n");
        code.append("syscall\n");
        return MIPSResult.createVoidResult();
      }

      default -> {
       code.append("# Save $ra to a register\n");
       String register = regAllocator.getT();
       code.append(String.format("move %s $ra\n", register));

       code.append("# Save $t0-9 registers\n");
       int allocatedSize = regAllocator.saveT(code, symbolTable.getTotalSize());

       code.append("# Evaluate parameters and save to stack\n");
       // TODO: Evaluate parameters and save to stack

       code.append("# Update the stack pointer\n");
       code.append(String.format("addi $sp $sp %d\n", -allocatedSize));

       code.append("# Call the function\n");
       code.append(String.format("jal %s\n", id));

       code.append("# Restore the stack pointer\n");
       code.append(String.format("addi $sp $sp %d\n", allocatedSize));

       code.append("# Restore $t0-9 registers\n");
       regAllocator.restoreT(code, symbolTable.getTotalSize());

       code.append("# Restore $ra\n");
       code.append(String.format("move $ra %s\n", register));

       regAllocator.clear(register);

       return MIPSResult.createVoidResult();
      }
    }
  }
}
