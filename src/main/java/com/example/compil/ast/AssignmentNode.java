package com.example.compil.ast;

public class AssignmentNode extends StatementNode {
    private String variable;
    private ExpressionNode expression;

    public AssignmentNode(String variable, ExpressionNode expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public String prettyPrint(int indent) {
        return indent(indent) + "Assignation: " + variable + " = " + expression.prettyPrint(0);
    }
}