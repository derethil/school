package submit.ast;

import java.util.List;

public class AndExpression implements Expression {
    List<Expression> unaryRelExpressions;

    public AndExpression(List<Expression> unaryRelExpressions) {
        this.unaryRelExpressions = unaryRelExpressions;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        for (Expression unaryRelExpression: unaryRelExpressions) {
            unaryRelExpression.toCminus(builder, prefix);
            builder.append(" && ");
        }

        if (unaryRelExpressions.size() > 0) {
            builder.delete(builder.length() - 4, builder.length());
        }
    }
}
