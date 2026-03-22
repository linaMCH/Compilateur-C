package com.example.compil.ast;

public class LiteralNode extends ExpressionNode {
    private Object value;

    public LiteralNode(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String prettyPrint(int indent) {
        return "  ".repeat(indent) + "Literal: " + value;
    }
}