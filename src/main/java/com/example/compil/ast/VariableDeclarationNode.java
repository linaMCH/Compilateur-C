package com.example.compil.ast;

public class VariableDeclarationNode extends StatementNode {
    private String type;
    private String name;

    public VariableDeclarationNode(String type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public String prettyPrint(int indent) {
        return indent(indent) + "Declaration: " + type + " " + name;
    }
}