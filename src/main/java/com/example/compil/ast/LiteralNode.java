package com.example.compil.ast;

public class LiteralNode extends ExpressionNode {
    private String value;

    public Object getValue() { return value; }

    public LiteralNode(String value) {
        this.value = value;
    }

    @Override
    public String prettyPrint(int indent) {
        return value;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}