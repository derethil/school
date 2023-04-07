package submit.ast;

import java.util.List;

public class Call implements Node, Expression {
    private final String id;
    private final List<Expression> args;

    public Call(String id, List<Expression> args) {
        this.id = id;
        this.args = args;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(id).append("(");

        for (Expression exp : args) {
            exp.toCminus(builder, prefix);
            builder.append(", ");
        }

        if (args.size() > 0)  builder.delete(builder.length() - 2, builder.length());

        builder.append(")");
    }
}
