package submit;

import org.antlr.v4.runtime.RuleContext;
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
            node = visitFunDeclaration(ctx.funDeclaration());
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

        VarType returnType = getVarType(ctx.typeSpecifier());
        symbolTable.addSymbol(ctx.ID().getText(), new SymbolInfo(
            ctx.ID().getText(),
            returnType,
            true
        ));

        String id = ctx.ID().getText();
        List<Param> params = new ArrayList<>();
        Statement statement = (Statement) visitStatement(ctx.statement());


        for (CminusParser.ParamContext paramCtx : ctx.param()) {
            Node param = visitParam(paramCtx);
            params.add((Param) param);
        }

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
            return visitExpressionStmt(ctx.expressionStmt());
        } else if (ctx.compoundStmt() != null) {
            return visitCompoundStmt(ctx.compoundStmt());
        } else if (ctx.ifStmt() != null) {
            return visitIfStmt(ctx.ifStmt());
        } else if (ctx.whileStmt() != null) {

        } else if (ctx.returnStmt() != null) {

        } else if (ctx.breakStmt() != null) {

        }

        return new Return(null);
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

        RuleContext parentCtx = ctx.parent;

        return new CompoundStatement(declarations, statements);

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
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitWhileStmt(CminusParser.WhileStmtContext ctx) { return visitChildren(ctx); }
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
                return visitMutable(ctx.mutable());
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
        return visitChildren(ctx);
    }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitAndExpression(CminusParser.AndExpressionContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitUnaryRelExpression(CminusParser.UnaryRelExpressionContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitRelExpression(CminusParser.RelExpressionContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitRelop(CminusParser.RelopContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitSumExpression(CminusParser.SumExpressionContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitSumop(CminusParser.SumopContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitTermExpression(CminusParser.TermExpressionContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitMulop(CminusParser.MulopContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitUnaryExpression(CminusParser.UnaryExpressionContext ctx) { return visitChildren(ctx); }
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
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
    @Override public Node visitMutable(CminusParser.MutableContext ctx) {
        Node mutable = new Mutable(ctx.ID().getText(), null);
        return mutable;
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
