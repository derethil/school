package submit.ast;

public enum ReturnType {

    INT("int"), BOOL("bool"), CHAR("char"), VOID("void");

    private final String value;

    private ReturnType(String value) {
        this.value = value;
    }


    public static ReturnType fromString(String s) {
        for (ReturnType vt : ReturnType.values()) {
            if (vt.value.equals(s)) {
                return vt;
            }
        }
        throw new RuntimeException("Illegal string in ReturnType.fromString()");
    }

    @Override
    public String toString() {
        return value;
    }

}