package com.example.compil.ast;

import java.util.List;

public   class IfNode extends StatementNode {
    private ExpressionNode condition;
    private List<StatementNode> thenBlock;
    private List<StatementNode> elseBlock;

    public ExpressionNode getCondition() { return condition; }
    public List<StatementNode> getThenBlock() { return thenBlock; }
    public List<StatementNode> getElseBlock() { return elseBlock; }

    public IfNode(ExpressionNode condition, List<StatementNode> thenBlock, List<StatementNode> elseBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public String prettyPrint(int indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(indent)).append("If:\n");
        sb.append(indent(indent + 1)).append("Condition: ").append(condition.prettyPrint(0)).append("\n");
        sb.append(indent(indent + 1)).append("Then:\n");
        for (StatementNode stmt : thenBlock) sb.append(stmt.prettyPrint(indent + 2)).append("\n");
        if (elseBlock != null) {
            sb.append(indent(indent + 1)).append("Else:\n");
            for (StatementNode stmt : elseBlock) sb.append(stmt.prettyPrint(indent + 2)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
