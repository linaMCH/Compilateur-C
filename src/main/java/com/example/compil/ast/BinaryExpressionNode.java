package com.example.compil.ast;

public class BinaryExpressionNode extends ExpressionNode {
    private ExpressionNode left;
    private String operator;
    private ExpressionNode right;

    public BinaryExpressionNode(ExpressionNode left, String operator, ExpressionNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String prettyPrint(int indent) {
        return "(" + left.prettyPrint(0) + " " + operator + " " + right.prettyPrint(0) + ")";
    }
}