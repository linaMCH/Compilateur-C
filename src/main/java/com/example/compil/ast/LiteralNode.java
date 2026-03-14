package com.example.compil.ast;

public class LiteralNode extends ExpressionNode {
    private String value;

    public LiteralNode(String value) {
        this.value = value;
    }

    @Override
    public String prettyPrint(int indent) {
        return value;
    }
}