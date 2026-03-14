package com.example.compil.ast;

public abstract class StatementNode {
    protected String indent(int n) {
        return "  ".repeat(Math.max(0, n));
    }
    public abstract String prettyPrint(int indent);
}