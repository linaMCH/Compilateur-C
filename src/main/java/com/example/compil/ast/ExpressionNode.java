package com.example.compil.ast;

public class ExpressionNode extends ASTNode {
    private String value;

    public ExpressionNode(String value) {
        this.value = value;
    }

    @Override
    public String prettyPrint(int indent) {
        return indent(indent) + "Expression: " + value;
    }
}