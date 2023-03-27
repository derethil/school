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
        builder.append(prefix);
        builder.append("if (");
        condition.toCminus(builder, prefix);
        builder.append(")\n");
        thenStatement.toCminus(builder, prefix);

        if (elseStatement != null) {
            builder.append(" else ");
            elseStatement.toCminus(builder, prefix);
        }
    }
}
