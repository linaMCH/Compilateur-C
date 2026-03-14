package com.example.compil.ast;

public class VariableNode extends ExpressionNode {
    private String name;

    public VariableNode(String name) {
        this.name = name;
    }

    @Override
    public String prettyPrint(int indent) {
        return name;
    }
}