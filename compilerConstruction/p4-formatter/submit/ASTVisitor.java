package submit;

import org.antlr.v4.runtime.tree.TerminalNode;
import parser.CminusBaseVisitor;
import parser.CminusParser;
import submit.ast.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ASTVisitor extends CminusBaseVisitor<Node> {
    private final Logger LOGGER;
    private SymbolTable symbolTable;

    public ASTVisitor(Logger LOGGER) {
        this.LOGGER = LOGGER;
    }

    private VarType getVarType(CminusParser.TypeSpecifierContext ctx) {
        final String t = ctx.getText();
        return (t.equals("int")) ? VarType.INT : (t.equals("bool")) ? VarType.BOOL : VarType.CHAR;
    }

    @Override public Node visitProgram(CminusParser.ProgramContext ctx) {
        symbolTable = new SymbolTable();
        List<Declaration> decls = new ArrayList<>();
        for (CminusParser.DeclarationContext d : ctx.declaration()) {
            decls.add((Declaration) visitDeclaration(d));
        }

        return new Program(decls);
    }

    @Override public Node visitVarDeclaration(CminusParser.VarDeclarationContext ctx) {
        VarType type = getVarType(ctx.typeSpecifier());
        List<String> ids = new ArrayList<>();
        List<Integer> arraySizes = new ArrayList<>();
        for (CminusParser.VarDeclIdContext v : ctx.varDeclId()) {
            String id = v.ID().getText();
            ids.add(id);
            symbolTable.addSymbol(id, new SymbolInfo(id, type, false));
            if (v.NUMCONST() != null) {
                arraySizes.add(Integer.parseInt(v.NUMCONST().getText()));
            } else {
                arraySizes.add(-1);
            }
        }
        final boolean isStatic = false;
        return new VarDeclaration(type, ids, arraySizes, isStatic);
    }

    @Override public Node visitReturnStmt(CminusParser.ReturnStmtContext ctx) {
        if (ctx.expression() != null) {
            return new Return((Expression) visitExpression(ctx.expression()));
        }
        return new Return(null);
    }

    @Override public Node visitConstant(CminusParser.ConstantContext ctx) {
        final Node node;
        if (ctx.NUMCONST() != null) {
            node = new NumConstant(Integer.parseInt(ctx.NUMCONST().getText()));
        } else if (ctx.CHARCONST() != null) {
            node = new CharConstant(ctx.CHARCONST().getText().charAt(0));
        } else if (ctx.STRINGCONST() != null) {
            node = new StringConstant(ctx.STRINGCONST().getText());
        } else {
            node = new BoolConstant(ctx.getText().equals("true"));
        }
        return node;
    }

    @Override public Node visitDeclaration(CminusParser.DeclarationContext ctx) {
        Node node;
        CminusParser.VarDeclarationContext varCtx = ctx.varDeclaration();
        if (varCtx != null) {
            node = visitVarDeclaration(varCtx);
        } else {
            symbolTable = symbolTable.createChild();
            node = visitFunDeclaration(ctx.funDeclaration());
            symbolTable = symbolTable.getParent();
        }

        return node;
    }

//    @Override public Node visitVarDeclId(CminusParser.VarDeclIdContext ctx) {
//        return visitChildren(ctx);
//    }

    @Override public Node visitFunDeclaration(CminusParser.FunDeclarationContext ctx) {
        TypeSpecifier type;
        if (ctx.typeSpecifier() == null) {
            type = null;
        } else {
            type = (TypeSpecifier) visitTypeSpecifier(ctx.typeSpecifier());
        }

        VarType returnType = type != null ? getVarType(ctx.typeSpecifier()) : null;
        symbolTable.getParent().addSymbol(ctx.ID().getText(), new SymbolInfo(
            ctx.ID().getText(),
            returnType,
            true
        ));

        String id = ctx.ID().getText();
        List<Param> params = new ArrayList<>();

        symbolTable = symbolTable.createChild();

        for (CminusParser.ParamContext paramCtx : ctx.param()) {
            Node param = visitParam(paramCtx);
            params.add((Param) param);

            SymbolInfo paramInfo = new SymbolInfo(
                    paramCtx.paramId().getChild(0).getText(),
                    getVarType(paramCtx.typeSpecifier()),
                    false
            );

            symbolTable.addSymbol(paramCtx.paramId().getChild(0).getText(), paramInfo);
        }

        Statement statement = (Statement) visitStatement(ctx.statement());
        symbolTable = symbolTable.getParent();

        return new FunDeclaration(type, id, params, statement);
    }

    @Override public Node visitTypeSpecifier(CminusParser.TypeSpecifierContext ctx) {
        return new TypeSpecifier(VarType.fromString(ctx.getText()));
    }

    @Override public Node visitParam(CminusParser.ParamContext ctx) {
        visitTypeSpecifier(ctx.typeSpecifier());
        VarType paramType = VarType.fromString(ctx.getChild(0).getText());
        Node paramId = visitParamId(ctx.paramId());
        return new Param(paramType, (ParamId) paramId);
    }

    @Override public Node visitParamId(CminusParser.ParamIdContext ctx) {
        boolean isArray = ctx.getChildCount() > 1;
        return new ParamId(ctx.ID().getText(), isArray);
    }

    @Override public Node visitStatement(CminusParser.StatementContext ctx) {
        Node node;
        if (ctx.expressionStmt() != null) {
            node = visitExpressionStmt(ctx.expressionStmt());
        } else if (ctx.compoundStmt() != null) {
            node = visitCompoundStmt(ctx.compoundStmt());
        } else if (ctx.ifStmt() != null) {
            node = visitIfStmt(ctx.ifStmt());
        } else if (ctx.whileStmt() != null) {
            node = visitWhileStmt(ctx.whileStmt());
        } else if (ctx.returnStmt() != null) {
            node = new Return((Expression) visitExpression(ctx.returnStmt().expression()));
        } else { // ctx.breakStmt() != null
            node = new Break();
        }

        return node;
    }

    @Override public Node visitCompoundStmt(CminusParser.CompoundStmtContext ctx) {
        symbolTable = symbolTable.createChild();

        List<VarDeclaration> declarations = new ArrayList<>();
        List<Statement> statements = new ArrayList<>();

        for (CminusParser.VarDeclarationContext varCtx : ctx.varDeclaration()) {
            Node node = visitVarDeclaration(varCtx);
            declarations.add((VarDeclaration) node);
        }

        for (CminusParser.StatementContext stmtCtx : ctx.statement()) {
            Node node = visitStatement(stmtCtx);
            statements.add((Statement) node);
        }
        symbolTable = symbolTable.getParent();

        boolean shouldIndent = !(ctx.parent.parent instanceof CminusParser.FunDeclarationContext);
        return new CompoundStatement(declarations, statements, shouldIndent);

    }

    @Override public Node visitExpressionStmt(CminusParser.ExpressionStmtContext ctx) {
        Node expression = visitExpression(ctx.expression());
        return new ExpressionStatement((Expression) expression);
    }
    @Override public Node visitIfStmt(CminusParser.IfStmtContext ctx) {
        Node expression = visitSimpleExpression(ctx.simpleExpression());

        List<CminusParser.StatementContext> statements= ctx.statement();
        if (statements.size() == 1) {
            return new IfStatement(
                    (Expression) expression,
                    (Statement) visitStatement(statements.get(0)),
                    null
            );
        } else {
            return new IfStatement(
                    (Expression) expression,
                    (Statement) visitStatement(statements.get(0)),
                    (Statement) visitStatement(statements.get(1))
            );
        }
    }

    @Override public Node visitWhileStmt(CminusParser.WhileStmtContext ctx) {
        Node expression = visitSimpleExpression(ctx.simpleExpression());
        Node statement = visitStatement(ctx.statement());
        return new WhileStatement((Expression) expression, (Statement) statement);
    }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitBreakStmt(CminusParser.BreakStmtContext ctx) { return visitChildren(ctx); }

    @Override public Node visitExpression(CminusParser.ExpressionContext ctx) {
        switch (ctx.getChildCount()) {
            case 1 -> {
                return visitSimpleExpression(ctx.simpleExpression());
            }
            case 2 -> {
                List<UnaryOperatorType> type = List.of(UnaryOperatorType.fromString(ctx.getChild(1).getText()));
                return new UnaryExpression((Expression) visitMutable(ctx.mutable()), type, false);
            }
            default -> {
                BinaryOperatorType type = BinaryOperatorType.fromString(ctx.getChild(1).getText());
                Node mutable = visitMutable(ctx.mutable());
                Node expression = visitExpression(ctx.expression());
                return new BinaryOperator((Mutable) mutable, type, (Expression) expression);
            }
        }
    }

    @Override public Node visitSimpleExpression(CminusParser.SimpleExpressionContext ctx) {
        return visitOrExpression(ctx.orExpression());
    }

    @Override public Node visitOrExpression(CminusParser.OrExpressionContext ctx) {
        List<Expression> andExpressions = new ArrayList<>();

        for (CminusParser.AndExpressionContext exprCtx: ctx.andExpression()) {
            andExpressions.add((Expression) visitAndExpression(exprCtx));
        }

        return new OrExpression(andExpressions);
    }

    @Override public Node visitAndExpression(CminusParser.AndExpressionContext ctx) {
        List<Expression> unaryRelExpressions = new ArrayList<>();
        for (CminusParser.UnaryRelExpressionContext unaryExpCtx: ctx.unaryRelExpression()) {
            unaryRelExpressions.add((Expression) visitUnaryRelExpression(unaryExpCtx));
        }
        return new AndExpression(unaryRelExpressions);
    }

    @Override public Node visitUnaryRelExpression(CminusParser.UnaryRelExpressionContext ctx) {
        Expression expression = (Expression) visitRelExpression(ctx.relExpression());
        if (ctx.BANG() == null) {
            return expression;
        } else {
            List<UnaryOperatorType> types = new ArrayList<>();
            for (TerminalNode bang: ctx.BANG()) {
                types.add(UnaryOperatorType.fromString(bang.getText()));
            }
            return new UnaryExpression(expression, types, true);
        }
    }

    @Override public Node visitRelExpression(CminusParser.RelExpressionContext ctx) {
        List<Expression> sumExpressions = new ArrayList<>();

        for (CminusParser.SumExpressionContext sumCtx: ctx.sumExpression()) {
            sumExpressions.add((Expression) visitSumExpression(sumCtx));
        }

        List<BinaryOperatorType> types = new ArrayList<>();

        for (CminusParser.RelopContext relopCtx: ctx.relop()) {
            types.add(BinaryOperatorType.fromString(relopCtx.getText()));
        }

        return new BinaryExpression(sumExpressions, types);
    }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitRelop(CminusParser.RelopContext ctx) { return visitChildren(ctx); }

    @Override public Node visitSumExpression(CminusParser.SumExpressionContext ctx) {
        List<Expression> termExpressions = new ArrayList<>();

        for (CminusParser.TermExpressionContext termCtx: ctx.termExpression()) {
            termExpressions.add((Expression) visitTermExpression(termCtx));
        }

        List<BinaryOperatorType> types = new ArrayList<>();

        for (CminusParser.SumopContext sumopCtx: ctx.sumop()) {
            types.add(BinaryOperatorType.fromString(sumopCtx.getText()));
        }

        return new BinaryExpression(termExpressions, types);
    }

//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitSumop(CminusParser.SumopContext ctx) { return visitChildren(ctx); }

    @Override public Node visitTermExpression(CminusParser.TermExpressionContext ctx) {
        List<Expression> unaryExpressions = new ArrayList<>();

        for (CminusParser.UnaryExpressionContext unaryExpCtx: ctx.unaryExpression()) {
            unaryExpressions.add((Expression) visitUnaryExpression(unaryExpCtx));
        }

        List<BinaryOperatorType> types = new ArrayList<>();

        for (CminusParser.MulopContext sumopCtx: ctx.mulop()) {
            types.add(BinaryOperatorType.fromString(sumopCtx.getText()));
        }

        return new BinaryExpression(unaryExpressions, types);
    }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitMulop(CminusParser.MulopContext ctx) { return visitChildren(ctx); }

    @Override public Node visitUnaryExpression(CminusParser.UnaryExpressionContext ctx) {
        List<UnaryOperatorType> types = new ArrayList<>();

        for (CminusParser.UnaryopContext unaryopCtx: ctx.unaryop()) {
            types.add(UnaryOperatorType.fromString(unaryopCtx.getText()));
        }

        return new UnaryExpression((Expression) visitFactor(ctx.factor()), types, true);
    }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitUnaryop(CminusParser.UnaryopContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitFactor(CminusParser.FactorContext ctx) { return visitChildren(ctx); }

    @Override public Node visitMutable(CminusParser.MutableContext ctx) {

        if (symbolTable.find(ctx.ID().getText()) == null) {
            LOGGER.warning("Undefined symbol on line " + ctx.getStart().getLine() + ": " + ctx.ID().getText());
        }

        if (ctx.expression() != null) {
            return new Mutable(ctx.ID().getText(), (Expression) visitExpression(ctx.expression()));
        } else {
            return new Mutable(ctx.ID().getText(), null);
        }
    }

    @Override public Node visitImmutable(CminusParser.ImmutableContext ctx) {
        if (ctx.expression() != null) {
            Node expression = visitExpression(ctx.expression());
            return new Immutable((Expression) expression);
        } else if (ctx.call() != null) {
            return visitCall(ctx.call());
        } else {
            return visitConstant(ctx.constant());
        }
    }

    @Override public Node visitCall(CminusParser.CallContext ctx) {
        if (symbolTable.find(ctx.ID().getText()) == null) {
            LOGGER.warning("Undefined symbol on line " + ctx.start.getLine() + ": " + ctx.ID().getText());
        }

        List<Expression> arguments = new ArrayList<>();

        for (CminusParser.ExpressionContext expressionContext : ctx.expression()) {
            arguments.add((Expression) visitExpression(expressionContext));
        }

        return new Call(ctx.ID().getText(), arguments);
    }
}
