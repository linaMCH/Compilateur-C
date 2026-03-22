package com.example.compil.ast;

import java.util.List;

public class SwitchNode extends StatementNode {
    private ExpressionNode expr;
    private List<CaseNode> cases;

    public ExpressionNode getExpression() { return expr;}

    public List<CaseNode> getCases() {
        return cases;
    }

    public SwitchNode(ExpressionNode expr, List<CaseNode> cases) {
        this.expr = expr;
        this.cases = cases;
    }

    @Override
    public String prettyPrint(int indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(indent)).append("Switch:\n");
        sb.append(indent(indent + 1)).append("Expression: ").append(expr.prettyPrint(0)).append("\n");
        for (CaseNode c : cases) sb.append(c.prettyPrint(indent + 1)).append("\n");
        return sb.toString();
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
