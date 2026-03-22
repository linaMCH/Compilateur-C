package com.example.compil.ast;


public class UnaryExpressionNode extends ExpressionNode {
    private String variable;
    private String operator;
    private boolean prefix; // true si préfixe
    private ExpressionNode expr;

    public String getVariable() {
        return variable;
    }

    public String getOperator() {
        return operator;
    }

    public boolean getPrefix() {
        return prefix;
    }

    public ExpressionNode getExpr() {
        return expr;
    }

    public UnaryExpressionNode(String variable, String operator, boolean prefix) {
        this(variable, operator, prefix, null);
    }

    public UnaryExpressionNode(String variable, String operator, boolean prefix, ExpressionNode expr) {
        this.variable = variable;
        this.operator = operator;
        this.prefix = prefix;
        this.expr = expr;
    }

    @Override
    public String prettyPrint(int indent) {
        if (expr != null) return (prefix ? operator : "") + expr.prettyPrint(0) + (prefix ? "" : operator);
        return (prefix ? operator : "") + variable + (prefix ? "" : operator);
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
