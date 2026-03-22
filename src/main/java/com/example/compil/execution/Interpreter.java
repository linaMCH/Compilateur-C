package com.example.compil.execution;

import com.example.compil.ast.*;

import java.util.ArrayList;
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
                case "/" -> {
                    if (r == 0) {
                        // On lance une exception personnalisée qui sera rattrapée par le Controller
                        throw new RuntimeException("Division par zéro impossible à la ligne " );
                    }
                    yield l / r; // On utilise yield dans un switch block pour retourner la valeur
                }
                case "==" -> l == r;
                // ... tes autres opérateurs
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

    @Override
    public Object visit(CaseNode node) {

        return null;
    }
    @Override
    public Object visit(SwitchNode node) {
        // 1. Évaluer l'expression du switch (ex: switch(x))
        Object switchValue = node.getExpression().accept(this);
        boolean foundMatch = false;

        // 2. Parcourir les cases
        for (CaseNode caseNode : node.getCases()) {
            // Un case null représente souvent le 'default'
            if (caseNode.getValue() == null) {
                executeCaseBody(caseNode);
                foundMatch = true;
                break;
            }

            Object caseValue = caseNode.getValue().accept(this);
            if (switchValue.equals(caseValue)) {
                executeCaseBody(caseNode);
                foundMatch = true;
                break; // On sort après le premier match (simulation simple sans fallthrough)
            }
        }
        return null;
    }
    private void executeCaseBody(CaseNode node) {
        for (StatementNode stmt : node.getStatements()) {
            stmt.accept(this);
        }
    }

    @Override
    public Object visit(ForNode node) {
        // 1. Initialisation (ex: int i = 0)
        if (node.getInit() != null) {
            node.getInit().accept(this);
        }

        while (true) {
            // 2. Condition (ex: i < 5)
            Object condition = node.getCondition().accept(this);
            boolean isTrue = false;
            if (condition instanceof Boolean) isTrue = (Boolean) condition;
            else if (condition instanceof Number) isTrue = ((Number) condition).doubleValue() != 0;

            if (!isTrue) break;

            // 3. Corps de la boucle
            if (node.getBody() != null) {
                for (StatementNode stmt : node.getBody()) {
                    stmt.accept(this);
                }
            }

            // 4. Incrémentation (ex: i++)
            if (node.getIncrement() != null) {
                node.getIncrement().accept(this);
            }
        }
        return null;
    }
    @Override
    public Object visit(DoWhileNode node) {
        boolean isTrue;
        do {
            // 1. Exécuter le corps
            for (StatementNode stmt : node.getBody()) {
                stmt.accept(this);
            }

            // 2. Vérifier la condition
            Object condition = node.getCondition().accept(this);
            isTrue = false;
            if (condition instanceof Boolean) isTrue = (Boolean) condition;
            else if (condition instanceof Number) isTrue = ((Number) condition).doubleValue() != 0;

        } while (isTrue);

        return null;
    }
    @Override public Object visit(ReturnNode node) { return null; }
    @Override
    public Object visit(UnaryExpressionNode node) {
        String op = node.getOperator();
        Object value;

        // 1. Déterminer la valeur à transformer
        if (node.getExpr() != null) {
            value = node.getExpr().accept(this);
        } else if (node.getVariable() != null) {
            if (!memory.containsKey(node.getVariable())) {
                throw new RuntimeException("Variable '" + node.getVariable() + "' non définie.");
            }
            value = memory.get(node.getVariable());
        } else {
            return null;
        }

        // 2. Appliquer l'opérateur
        if (value instanceof Number) {
            double d = ((Number) value).doubleValue();

            switch (op) {
                case "++":
                    double postInc = d + 1;
                    if (node.getVariable() != null) memory.put(node.getVariable(), postInc);
                    return node.getPrefix() ? postInc : d;
                case "--":
                    double postDec = d - 1;
                    if (node.getVariable() != null) memory.put(node.getVariable(), postDec);
                    return node.getPrefix() ? postDec : d;
                case "-":
                    return -d;
                default:
                    return d;
            }
        } else if (value instanceof Boolean) {
            boolean b = (Boolean) value;
            return op.equals("!") ? !b : b;
        }

        return null;
    }
    @Override
    public Object visit(UnaryStatementNode node) {
        String varName = node.getVariableName();
        if (!memory.containsKey(varName)) {
            throw new RuntimeException("Erreur : Variable '" + varName + "' non définie.");
        }

        double currentVal = ((Number) memory.get(varName)).doubleValue();
        String op = node.getOperator();

        double newVal = switch (op) {
            case "++" -> currentVal + 1;
            case "--" -> currentVal - 1;
            default -> currentVal;
        };

        memory.put(varName, newVal);
        return newVal;
    }
    @Override public Object visit(ExpressionNode node) { return null; }
    private final List<String> consoleOutput = new ArrayList<>();

    public List<String> getConsoleOutput() {
        return consoleOutput;
    }

    @Override
    public Object visit(PrintNode node) {
        Object value = node.getExpression().accept(this);
        consoleOutput.add(String.valueOf(value));
        return value;
    }
}