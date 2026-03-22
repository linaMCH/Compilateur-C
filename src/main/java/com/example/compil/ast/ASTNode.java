package com.example.compil.ast;

public abstract class ASTNode {
    public abstract String prettyPrint(int indent);

    public abstract Object accept(ASTVisitor visitor);

    protected String indent(int n) {
        return "  ".repeat(n);
    }

    @Override
    public String toString() {
        return prettyPrint(0);
    }

}