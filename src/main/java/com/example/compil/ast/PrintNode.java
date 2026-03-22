package com.example.compil.ast;

public class PrintNode extends StatementNode {
    private ExpressionNode expression;

    public PrintNode(ExpressionNode expression) {
        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String prettyPrint(int indent) {
        return "  ".repeat(indent) + "Print: " + expression.prettyPrint(0);
    }
}