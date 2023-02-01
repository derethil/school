public class Infix {
    int lookaheadIdx = 0;
    char lookahead;
    String terminals;

    public static void main(String[] args) {
        String str = "+++12-835";
        if (args.length > 0) {
            str = args[0];
        }
        try {
            Infix parser = new Infix(str);
            parser.parse();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Infix(String terminals) {
        lookahead = terminals.charAt(lookaheadIdx);
        this.terminals = terminals;
    }

    private void parse() {
        list();
        if (lookahead != '$') throw new RuntimeException("error");
    }

    private void match(char terminal) {
        if (lookahead == terminal) {
            lookaheadIdx++;
            if (lookaheadIdx >= terminals.length()) {
                lookahead = '$';
            } else {
                lookahead = terminals.charAt(lookaheadIdx);
            }
        } else {
            throw new RuntimeException("error");
        }
    }

    private void list() {
        switch (lookahead) {
            case '+', '-' -> {
                char symbol = lookahead;
                match(symbol);
                System.out.print("(");
                list();
                System.out.print(symbol);
                list();
                System.out.print(")");
            }

            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                digit();
                match(lookahead);
            }

            default -> {
                throw new RuntimeException("error");
            }
        }
    }

    private void digit() {
        System.out.print(lookahead);
    }
}
