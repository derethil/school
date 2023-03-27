package submit.ast;

import java.util.List;

public class OrExpression implements Expression {
    private List<Expression> andExpressions;

    public OrExpression(List<Expression> andExpressions) {
        this.andExpressions = andExpressions;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        for (Expression andExpression : andExpressions) {
            andExpression.toCminus(builder, prefix);
            builder.append(" || ");
        }

        if (andExpressions.size() > 0) {
            builder.delete(builder.length() - 4, builder.length());
        }
    }
}
