package com.example.compil.ast;

public class VariableDeclarationNode extends StatementNode {

    private String type;
    private String variable;
    private ExpressionNode initializer;

    public String getType () { return type;}
    public String getVariableName () { return variable;}
    public ExpressionNode getInitializer () { return initializer;}

    public VariableDeclarationNode(String type, String variable) {
        this(type, variable, null);
    }

    public VariableDeclarationNode(String type, String variable, ExpressionNode initializer) {
        this.type = type;
        this.variable = variable;
        this.initializer = initializer;
    }

    @Override
    public String prettyPrint(int indent) {
        String ind = indent(indent);
        String result = ind + "Declaration: " + type + " " + variable;
        if (initializer != null) {
            result += " = " + initializer.prettyPrint(0);
        }
        return result;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}