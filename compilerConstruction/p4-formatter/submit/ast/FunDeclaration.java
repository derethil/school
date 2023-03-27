package submit.ast;

import java.util.List;

public class FunDeclaration implements Declaration, Node {
    private final TypeSpecifier returnType;
    private final String id;
    private final List<Param> parameters;
    private final Statement statement;

    public FunDeclaration(TypeSpecifier returnType, String id, List<Param> parameters, Statement statement) {
        this.returnType = returnType;
        this.id = id;
        this.parameters = parameters;
        this.statement = statement;
    }

    @Override
    public void toCminus(StringBuilder builder, final String prefix) {
        builder.append("\n");
        builder.append(prefix);

        if (returnType == null) {
            builder.append("void").append(" ");
        } else {
            returnType.toCminus(builder, prefix);
            builder.append(" ");
        }

        builder.append(id).append("(");

        for (Param parameter : parameters) {
            parameter.toCminus(builder, prefix);
            builder.append(", ");
        }

        if (parameters.size() > 0) {
            builder.delete(builder.length() - 2, builder.length());
        }

        builder.append(")\n");

        builder.append(prefix);
        statement.toCminus(builder, prefix + "  ");
    }
}
