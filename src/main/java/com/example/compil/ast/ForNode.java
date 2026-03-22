package com.example.compil.ast;

import java.util.List;

public  class ForNode extends StatementNode {
    private StatementNode init;
    private ExpressionNode condition;
    private StatementNode increment;
    private List<StatementNode> body;

    public StatementNode getInit() { return init; }
    public ExpressionNode getCondition() { return condition; }
    public StatementNode getIncrement() { return increment; }
    public List<StatementNode> getBody() { return body; }

    public ForNode(StatementNode init, ExpressionNode condition, StatementNode increment, List<StatementNode> body) {
        this.init = init;
        this.condition = condition;
        this.increment = increment;
        this.body = body;
    }

    @Override
    public String prettyPrint(int indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(indent)).append("For:\n");
        sb.append(indent(indent + 1)).append("Init: ").append(init.prettyPrint(0)).append("\n");
        sb.append(indent(indent + 1)).append("Condition: ").append(condition.prettyPrint(0)).append("\n");
        sb.append(indent(indent + 1)).append("Increment: ").append(increment.prettyPrint(0)).append("\n");
        sb.append(indent(indent + 1)).append("Body:\n");
        for (StatementNode stmt : body) sb.append(stmt.prettyPrint(indent + 2)).append("\n");
        return sb.toString();
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
