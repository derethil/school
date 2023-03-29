package submit.ast;

public class WhileStatement implements Statement {
    private final Expression condition;
    private final Statement statement;

    public WhileStatement(Expression condition, Statement statement) {
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(prefix);
        builder.append("while (");
        condition.toCminus(builder, prefix);
        builder.append(")\n");
        statement.toCminus(builder, statement.isCompound() ? prefix : prefix + " ");
        builder.append("\n");
    }
}
