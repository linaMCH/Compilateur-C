package com.example.compil.ast;

import java.util.List;

public class DoWhileNode extends StatementNode {
    private ExpressionNode condition;
    private List<StatementNode> body;

    public ExpressionNode getCondition() { return condition; }
    public List<StatementNode> getBody() { return body; }

    public DoWhileNode(ExpressionNode condition, List<StatementNode> body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public String prettyPrint(int indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(indent)).append("DoWhile:\n");
        sb.append(indent(indent + 1)).append("Body:\n");
        for (StatementNode stmt : body) sb.append(stmt.prettyPrint(indent + 2)).append("\n");
        sb.append(indent(indent + 1)).append("Condition: ").append(condition.prettyPrint(0));
        return sb.toString();
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}