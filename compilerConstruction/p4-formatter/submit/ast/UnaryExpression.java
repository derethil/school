package submit.ast;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

public class UnaryExpression implements Expression {
    private final Expression expression;
    private final List<UnaryOperatorType> operators;
    private final boolean isPrefix;

    public UnaryExpression(Expression expression, List<UnaryOperatorType> operators, boolean isPrefix) {
        this.expression = expression;
        this.operators = operators;
        this.isPrefix = isPrefix;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        if (isPrefix) {
            for (UnaryOperatorType operator : operators) {
                builder.append(operator);
            }
        }

        expression.toCminus(builder, prefix);

        if (!isPrefix) {
            for (UnaryOperatorType operator : operators) {
                builder.append(operator);
            }
        }
    }
}
