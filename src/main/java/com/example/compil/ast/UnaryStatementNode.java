package com.example.compil.ast;

public class UnaryStatementNode extends StatementNode {
    private String variable;
    private String operator;
    private boolean prefix;

    public UnaryStatementNode(String variable, String operator, boolean prefix) {
        this.variable = variable;
        this.operator = operator;
        this.prefix = prefix;
    }

    @Override
    public String prettyPrint(int indent) {
        return indent(indent) + "UnaryStmt: " + (prefix ? operator + variable : variable + operator);
    }
}