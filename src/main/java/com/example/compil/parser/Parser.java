package com.example.compil.parser;

import com.example.compil.Token;
import com.example.compil.ast.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private List<Token> tokens;
    private int position = 0;
    private List<StatementNode> statements = new ArrayList<>();
    private List<String> errors = new ArrayList<>();

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<String> getErrors() {
        return errors;
    }

    private Token peek() {
        if (position < tokens.size()) return tokens.get(position);
        return null;
    }

    private Token consume() {
        if (position < tokens.size()) return tokens.get(position++);
        return null;
    }

    private boolean match(String type) {
        Token t = peek();
        if (t != null && t.getType().equals(type)) {
            consume();
            return true;
        }
        return false;
    }

    private void error(String message, Token token) {
        String lineInfo = (token != null) ? "ligne " + token.getLine() : "";
        errors.add("Erreur syntaxique : " + message + " " + lineInfo);
    }

    public List<StatementNode> parse() {
        while (peek() != null) {
            StatementNode stmt = parseStatement();
            if (stmt != null) {
                statements.add(stmt);
            } else {
                Token t = peek();
                if (t != null) {
                    error("token inattendu '" + t.getValue() + "'", t);
                    consume();
                } else {
                    error("token inattendu EOF", null);
                }
            }
        }
        return statements;
    }

    private StatementNode parseStatement() {
        Token t = peek();
        if (t == null) return null;

        switch (t.getType()) {
            case "INT":
            case "FLOAT":
            case "DOUBLE":
            case "CHAR":
            case "CONST":
            case "VOID":
                return parseVariableDeclaration();

            case "ID":
                return parseAssignment();

            case "IF":
            case "WHILE":
                return parseControlStatement();

            default:
                return null;
        }
    }

    private StatementNode parseVariableDeclaration() {
        Token typeToken = consume(); // int, float, etc.
        Token idToken = consume();
        if (idToken == null || !idToken.getType().equals("ID")) {
            error("identificateur attendu", idToken);
            return null;
        }
        if (!match("SEMICOL")) {
            Token next = peek();
            error("';' attendu", next);
            return null;
        }
        return new VariableDeclarationNode(typeToken.getValue(), idToken.getValue());
    }

    private StatementNode parseAssignment() {
        Token idToken = consume(); // variable
        if (!match("AFFECT")) {
            error("'=' attendu", peek());
            return null;
        }
        ExpressionNode expr = parseExpression();
        if (!match("SEMICOL")) {
            error("';' attendu", peek());
            return null;
        }
        return new AssignmentNode(idToken.getValue(), expr);
    }

    // ================== Expressions ==================
    private ExpressionNode parseExpression() {
        return parseLogical();
    }

    private ExpressionNode parseLogical() {
        ExpressionNode left = parseComparison();
        while (peek() != null && (peek().getType().equals("AND") || peek().getType().equals("OR"))) {
            Token op = consume();
            ExpressionNode right = parseComparison();
            left = new BinaryExpressionNode(left, op.getValue(), right);
        }
        return left;
    }

    private ExpressionNode parseComparison() {
        ExpressionNode left = parseAddSub();
        while (peek() != null && (peek().getType().equals("EQL") || peek().getType().equals("INF")
                || peek().getType().equals("SUP") || peek().getType().equals("INF_EQL")
                || peek().getType().equals("SUP_EQL"))) {
            Token op = consume();
            ExpressionNode right = parseAddSub();
            left = new BinaryExpressionNode(left, op.getValue(), right);
        }
        return left;
    }

    private ExpressionNode parseAddSub() {
        ExpressionNode left = parseMulDiv();
        while (peek() != null && (peek().getType().equals("PLUS") || peek().getType().equals("MOINS"))) {
            Token op = consume();
            ExpressionNode right = parseMulDiv();
            left = new BinaryExpressionNode(left, op.getValue(), right);
        }
        return left;
    }

    private ExpressionNode parseMulDiv() {
        ExpressionNode left = parsePrimary();
        while (peek() != null && (peek().getType().equals("MULT") || peek().getType().equals("DIV"))) {
            Token op = consume();
            ExpressionNode right = parsePrimary();
            left = new BinaryExpressionNode(left, op.getValue(), right);
        }
        return left;
    }

    private ExpressionNode parsePrimary() {
        Token t = peek();
        if (t == null) return null;

        if (t.getType().equals("NUM_INT") || t.getType().equals("NUM_FLOAT")) {
            consume();
            return new LiteralNode(t.getValue());
        } else if (t.getType().equals("ID")) {
            consume();
            return new VariableNode(t.getValue());
        } else if (t.getType().equals("OUV_PAREN")) {
            consume();
            ExpressionNode expr = parseExpression();
            if (!match("FER_PAREN")) {
                error("')' attendu", peek());
            }
            return expr;
        } else {
            error("expression inattendue", t);
            consume();
            return null;
        }
    }

    // ================== Contrôle (if/while) ==================
    private StatementNode parseControlStatement() {
        Token t = consume(); // IF ou WHILE
        if (!match("OUV_PAREN")) {
            error("'(' attendu", peek());
            return null;
        }
        ExpressionNode condition = parseExpression();
        if (!match("FER_PAREN")) {
            error("')' attendu", peek());
            return null;
        }
        if (!match("CROCH_OUV")) {
            error("'{' attendu", peek());
            return null;
        }

        List<StatementNode> block = new ArrayList<>();
        while (!match("CROCH_FER") && peek() != null) {
            StatementNode stmt = parseStatement();
            if (stmt != null) block.add(stmt);
        }

        return new StatementNode() {
            @Override
            public String prettyPrint(int indent) {
                StringBuilder sb = new StringBuilder();
                sb.append(indent(indent)).append(t.getValue())
                        .append(" (").append(condition.prettyPrint(0)).append(") {\n");
                for (StatementNode s : block) {
                    sb.append(s.prettyPrint(indent + 1)).append("\n");
                }
                sb.append(indent(indent)).append("}");
                return sb.toString();
            }
        };
    }
}