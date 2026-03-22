package com.example.compil.execution;

import com.example.compil.ast.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Interpreter implements ASTVisitor {

    private final Map<String, Object> memory = new HashMap<>();

    public Map<String, Object> getMemory() {
        return memory;
    }

    @Override
    public Object visit(LiteralNode node) {
        return node.getValue();
    }

    @Override
    public Object visit(VariableNode node) {
        String name = node.getName();
        if (memory.containsKey(name)) {
            return memory.get(name);
        }
        throw new RuntimeException("Erreur : Variable '" + name + "' non définie.");
    }

    @Override
    public Object visit(VariableDeclarationNode node) {
        Object initialValue = 0.0;
        if (node.getInitializer() != null) {
            initialValue = node.getInitializer().accept(this);
        }
        memory.put(node.getVariableName(), initialValue);
        return initialValue;
    }

    @Override
    public Object visit(AssignmentNode node) {
        Object value = node.getExpression().accept(this);
        memory.put(node.getVariableName(), value);
        return value;
    }

    @Override
    public Object visit(BinaryExpressionNode node) {
        Object leftVal = node.getLeft().accept(this);
        Object rightVal = node.getRight().accept(this);

        if (leftVal instanceof Number && rightVal instanceof Number) {
            double l = ((Number) leftVal).doubleValue();
            double r = ((Number) rightVal).doubleValue();
            String op = node.getOperator();

            return switch (op) {
                case "+" -> l + r;
                case "-" -> l - r;
                case "*" -> l * r;
                case "/" -> (r == 0) ? 0.0 : l / r;
                case "==" -> l == r;
                case "!=" -> l != r;
                case "<" -> l < r;
                case ">" -> l > r;
                case "<=" -> l <= r;
                case ">=" -> l >= r;
                default -> 0.0;
            };
        }
        throw new RuntimeException("Opération impossible sur des types non numériques.");
    }

    @Override
    public Object visit(IfNode node) {
        Object condition = node.getCondition().accept(this);
        boolean isTrue = false;
        if (condition instanceof Boolean) isTrue = (Boolean) condition;
        else if (condition instanceof Number) isTrue = ((Number) condition).doubleValue() != 0;

        if (isTrue) {
            for (StatementNode stmt : node.getThenBlock()) stmt.accept(this);
        } else if (node.getElseBlock() != null) {
            for (StatementNode stmt : node.getElseBlock()) stmt.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(WhileNode node) {
        while (true) {
            Object condition = node.getCondition().accept(this);
            boolean isTrue = false;
            if (condition instanceof Boolean) isTrue = (Boolean) condition;
            else if (condition instanceof Number) isTrue = ((Number) condition).doubleValue() != 0;

            if (!isTrue) break;
            for (StatementNode stmt : node.getBody()) stmt.accept(this);
        }
        return null;
    }

    @Override public Object visit(CaseNode node) { return null; }
    @Override public Object visit(SwitchNode node) { return null; }
    @Override public Object visit(ForNode node) { return null; }
    @Override public Object visit(DoWhileNode node) { return null; }
    @Override public Object visit(ReturnNode node) { return null; }
    @Override public Object visit(UnaryExpressionNode node) { return null; }
    @Override public Object visit(UnaryStatementNode node) { return null; }
    @Override public Object visit(ExpressionNode node) { return null; }
}