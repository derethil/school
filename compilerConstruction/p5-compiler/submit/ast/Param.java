package submit.ast;

public class Param implements Node {
    private final VarType paramType;
    private final ParamId id;

    public Param(VarType paramType, ParamId id) {
        this.paramType = paramType;
        this.id = id;
    }

    @Override
    public void toCminus(StringBuilder builder, final String prefix) {
        builder.append(prefix);
        builder.append(paramType.toString()).append(" ");
        id.toCminus(builder, prefix);

    }
}
