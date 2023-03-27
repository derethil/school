package submit.ast;

public enum UnaryOperatorType {
    INCREMENT("++"), DECREMENT("--"),
    NOT("!"),
    MINUS("-"), DEREFERENCE("*"), QUESTION_MARK("?");

    private final String value;

    UnaryOperatorType(String value) {
        this.value = value;
    }

    public static UnaryOperatorType fromString(String value) {
        for (UnaryOperatorType type : UnaryOperatorType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new RuntimeException("Illegal string in IncrementOperatorType.fromString(): " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}
