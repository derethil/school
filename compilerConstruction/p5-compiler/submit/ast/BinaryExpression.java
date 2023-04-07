package submit.ast;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class BinaryExpression implements Expression {
    private final List<Expression> expressions;
    private final List<BinaryOperatorType> operators;
    private List<TerminalNode> prefixingOperators = new ArrayList<>();

    public BinaryExpression(List<Expression> expressions, List<BinaryOperatorType> operators) {
        this.expressions = expressions;
        this.operators = operators;
    }

    public BinaryExpression(List<Expression> expressions, List<BinaryOperatorType> operators, List<TerminalNode> prefixingOperators) {
        this.expressions = expressions;
        this.operators = operators;
        this.prefixingOperators = prefixingOperators;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        for (int i = 0; i < prefixingOperators.size(); i++) {
            builder.append(prefixingOperators.get(i));
        }
        for (int i = 0; i < expressions.size(); i++) {
            if (i > 0) {
                builder.append(" ").append(operators.get(i - 1)).append(" ");
            }
            expressions.get(i).toCminus(builder, prefix);
        }
    }
}
