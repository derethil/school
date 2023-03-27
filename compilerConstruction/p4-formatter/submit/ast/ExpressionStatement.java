package submit.ast;

public class ExpressionStatement implements Statement {
    private Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(prefix);

        if (expression != null) {
            expression.toCminus(builder, prefix);
        }

        builder.append(";\n");
    }
}
