package submit.ast;

import java.util.List;

public class CompoundStatement implements Statement {
    private final List<VarDeclaration> declarations;
    private final List<Statement> statements;
    private boolean shouldAppendPrefix = true;

    public CompoundStatement(List<VarDeclaration> declarations, List<Statement> statements) {
        this.declarations = declarations;
        this.statements = statements;
    }

    public CompoundStatement(List<VarDeclaration> declarations, List<Statement> statements, boolean shouldAppendPrefix) {
        this(declarations, statements);
        this.shouldAppendPrefix = shouldAppendPrefix;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        if (shouldAppendPrefix) builder.append(prefix);
        builder.append("{").append("\n");

        for (VarDeclaration varDeclaration: declarations) {
            varDeclaration.toCminus(builder, prefix + "  ");
        }

        for (Statement statement: statements) {
            statement.toCminus(builder, prefix + "  ");
            if (statement instanceof ExpressionStatement || statement instanceof Break) builder.append("\n");
        }

        if(shouldAppendPrefix) builder.append(prefix);
        builder.append("}");

        if (declarations.size() == 0 && statements.size() == 0) builder.append("\n");
    }

    @Override
    public boolean isCompound() {
        return true;
    }
}
