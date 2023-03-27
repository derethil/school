package submit.ast;

public enum  AssignmentType {
    EQUALS("="), PLUS_EQUALS("+="), MINUS_EQUALS("-="), TIMES_EQUALS("*="), DIVIDE_EQUALS("/=");

    private final String value;

    private AssignmentType(String value) {
        this.value = value;
    }

    public static AssignmentType fromString(String s) {
        for (AssignmentType at : AssignmentType.values()) {
            if (at.value.equals(s)) {
                return at;
            }
        }
        throw new RuntimeException("Illegal string in AssignmentType.fromString(): " + s);
    }

    @Override
    public String toString() {
        return value;
    }
}
