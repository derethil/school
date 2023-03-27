package submit.ast;

import java.util.List;

public class CompoundStatement implements Statement {
    private final List<VarDeclaration> declarations;
    private final List<Statement> statements;

    public CompoundStatement(List<VarDeclaration> declarations, List<Statement> statements) {
        this.declarations = declarations;
        this.statements = statements;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append("{").append("\n");

        for (VarDeclaration varDeclaration: declarations) {
            varDeclaration.toCminus(builder, prefix);
        }

        for (Statement statement: statements) {
            statement.toCminus(builder, prefix);
        }

        builder.append("\n}");

    }
}
