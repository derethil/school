package submit.ast;

public interface Node extends AbstractNode {
    void toCminus(StringBuilder builder, final String prefix);
}
