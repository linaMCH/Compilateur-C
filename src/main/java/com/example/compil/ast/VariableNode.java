package com.example.compil.ast;

public class VariableNode extends ExpressionNode {
    private String name;
    public String getName() { return name; }

    public VariableNode(String name) {
        this.name = name;
    }

    @Override
    public String prettyPrint(int indent) {
        return name;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}