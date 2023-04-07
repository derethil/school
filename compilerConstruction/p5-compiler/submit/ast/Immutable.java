package submit.ast;

public class Immutable implements Node, Expression {
    private final Expression expression;
    public Immutable(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append("(");
        expression.toCminus(builder, prefix);
        builder.append(")");
    }
}
