package submit.ast;

public class Assignment implements Node, Expression {
    private final Mutable lhs;
    private final Expression rhs;
    private final AssignmentType type;

    public Assignment(Mutable lhs, AssignmentType type, Expression rhs) {
        this.lhs = lhs;
        this.type = type;
        this.rhs = rhs;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        lhs.toCminus(builder, prefix);
        builder.append(" ").append(type).append(" ");
        rhs.toCminus(builder, prefix);
    }
}
