package com.example.compil.ast;

import java.util.List;

    public class CaseNode extends StatementNode {
        private ExpressionNode value;
        private List<StatementNode> body;

        public ExpressionNode getValue() { return value; }
        public List<StatementNode> getStatements() { return body; }

        public CaseNode(ExpressionNode value, List<StatementNode> body) {
            this.value = value;
            this.body = body;
        }

        @Override
        public String prettyPrint(int indent) {
            StringBuilder sb = new StringBuilder();
            sb.append(indent(indent)).append("Case: ").append(value.prettyPrint(0)).append("\n");
            for (StatementNode stmt : body) sb.append(stmt.prettyPrint(indent + 1)).append("\n");
            return sb.toString();
        }

        @Override
        public Object accept(ASTVisitor visitor) {
            return visitor.visit(this);
        }
    }
