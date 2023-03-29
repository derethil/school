package submit.ast;

public class IfStatement implements Statement {
    private final Expression condition;
    private final Statement thenStatement;
    private final Statement elseStatement;

    public IfStatement(Expression condition, Statement thenStatement, Statement elseStatement) {
        this.condition = condition;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(prefix).append("if (");
        condition.toCminus(builder, prefix);
        builder.append(")\n");
        thenStatement.toCminus(builder, thenStatement.isCompound() ? prefix : prefix + " ");
        if (elseStatement == null && (thenStatement instanceof ExpressionStatement || thenStatement instanceof CompoundStatement)) builder.append("\n");
        else if (elseStatement != null) {
            builder.append("\n").append(prefix);
            builder.append("else\n");
            elseStatement.toCminus(builder, elseStatement.isCompound() ? prefix : prefix + " ");
            if (elseStatement instanceof ExpressionStatement) builder.append("\n");
        }
    }
}
