package submit.ast;

public class TypeSpecifier implements Node, Expression {
    private final VarType type;

    public TypeSpecifier(VarType type) {
        this.type = type;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(prefix);
        builder.append(type);
    }
}
