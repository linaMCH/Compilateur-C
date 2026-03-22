package com.example.compil.ast;

public class BinaryExpressionNode extends ExpressionNode {
    private ExpressionNode left;
    private String operator;
    private ExpressionNode right;

    public ExpressionNode getLeft() { return left; }
    public ExpressionNode getRight() { return right; }
    public String getOperator() { return operator; }

    public BinaryExpressionNode(ExpressionNode left, String operator, ExpressionNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String prettyPrint(int indent) {
        return "(" + left.prettyPrint(0) + " " + operator + " " + right.prettyPrint(0) + ")";
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}