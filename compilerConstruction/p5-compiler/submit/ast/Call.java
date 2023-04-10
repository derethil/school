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
    code.append("# ").append(id).append("\n");
    switch (id) {
      case "println" -> {
        MIPSResult result = args.get(0).toMIPS(code, data, symbolTable, regAllocator);
        VarType type = result.getType();

        switch (type) {
          case INT -> {
            code.append("move $a0 ").append(result.getRegister()).append("\n");
            code.append("li $v0 1\n");
            regAllocator.clear(result.getRegister());
          }
          case CHAR -> {
            code.append("la $a0 ").append(result.getAddress()).append("\n");
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
       return null;
      }
    }
  }
}
