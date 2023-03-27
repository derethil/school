package submit.ast;

public class ParamId implements Node{
    private String id;
    private boolean isArray;

    public ParamId(String id, boolean isArray) {
        this.id = id;
        this.isArray = isArray;
    }

    @Override
    public void toCminus(StringBuilder builder, final String prefix) {
        builder.append(id);

        if (isArray) {
            builder.append("[]");
        }
    }
}
