package com.example.compil.ast;

public class ReturnNode extends StatementNode {
    private ExpressionNode expr;

    public ExpressionNode getExpr() { return expr; }

    public ReturnNode(ExpressionNode expr) {
        this.expr = expr;
    }

    @Override
    public String prettyPrint(int indent) {
        return indent(indent) + "Return: " + (expr != null ? expr.prettyPrint(0) : "void");
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}