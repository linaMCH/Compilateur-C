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

    // ================== Statements ==================
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
                return parseAssignmentOrUnary();

            case "IF":
            case "WHILE":
            case "FOR":
            case "DO":
                return parseControlStatement();

            case "SWITCH":
                return parseSwitchStatement();

            case "RETURN":
                return parseReturnStatement();

            default:
                return null;
        }
    }

    // ================== Déclarations ==================
    private StatementNode parseVariableDeclaration() {
        Token typeToken = consume();
        Token idToken = consume();

        if (idToken == null || !idToken.getType().equals("ID")) {
            error("identificateur attendu", idToken);
            return null;
        }

        ExpressionNode initializer = null;

        // Si il y a un '=' juste après la déclaration
        if (peek() != null && peek().getType().equals("AFFECT")) {
            consume(); // consomme '='
            initializer = parseExpression();
        }

        if (!match("SEMICOL")) {
            error("';' attendu", peek());
            return null;
        }

        return new VariableDeclarationNode(typeToken.getValue(), idToken.getValue(), initializer);
    }

    // ================== Assignation / Unary ==================
    private StatementNode parseAssignmentOrUnary() {
        Token idToken = consume(); // variable
        if (peek() != null && peek().getType().equals("AFFECT")) {
            consume();
            ExpressionNode expr = parseExpression();
            if (!match("SEMICOL")) error("';' attendu", peek());
            return new AssignmentNode(idToken.getValue(), expr);
        } else if (peek() != null && (peek().getType().equals("INC") || peek().getType().equals("DEC"))) {
            Token op = consume();
            if (!match("SEMICOL")) error("';' attendu", peek());
            return new UnaryStatementNode(idToken.getValue(), op.getValue(), false); // ← utilise StatementNode
        } else {
            error("expression ou assignation attendue", idToken);
            return null;
        }
    }

    // ================== Return ==================
    private StatementNode parseReturnStatement() {
        consume(); // RETURN
        ExpressionNode expr = parseExpression();
        if (!match("SEMICOL")) error("';' attendu", peek());
        return new ReturnNode(expr);
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
        ExpressionNode left = parseUnary();
        while (peek() != null && (peek().getType().equals("MULT") || peek().getType().equals("DIV"))) {
            Token op = consume();
            ExpressionNode right = parseUnary();
            left = new BinaryExpressionNode(left, op.getValue(), right);
        }
        return left;
    }

    private ExpressionNode parseUnary() {
        Token t = peek();
        if (t != null && t.getType().equals("NOT")) {
            consume();
            ExpressionNode expr = parseUnary();
            return new UnaryExpressionNode(null, "!", true, expr);
        } else if (t != null && (t.getType().equals("INC") || t.getType().equals("DEC"))) {
            consume();
            ExpressionNode expr = parsePrimary();
            return new UnaryExpressionNode(null, t.getValue(), true, expr);
        } else {
            return parsePrimary();
        }
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
            if (!match("FER_PAREN")) error("')' attendu", peek());
            return expr;
        } else {
            error("expression inattendue", t);
            consume();
            return null;
        }
    }

    // ================== Contrôle ==================
    private StatementNode parseControlStatement() {
        Token t = consume(); // IF, WHILE, FOR, DO
        switch (t.getType()) {
            case "IF":
                return parseIfStatement();
            case "WHILE":
                return parseWhileStatement();
            case "FOR":
                return parseForStatement();
            case "DO":
                return parseDoWhileStatement();
        }
        return null;
    }

    private StatementNode parseIfStatement() {
        if (!match("OUV_PAREN")) { error("'(' attendu", peek()); return null; }
        ExpressionNode cond = parseExpression();
        if (!match("FER_PAREN")) { error("')' attendu", peek()); return null; }
        if (!match("CROCH_OUV")) { error("'{' attendu", peek()); return null; }
        List<StatementNode> block = parseBlock();
        List<StatementNode> elseBlock = null;
        if (peek() != null && peek().getType().equals("ELSE")) {
            consume();
            if (!match("CROCH_OUV")) { error("'{' attendu après else", peek()); return null; }
            elseBlock = parseBlock();
        }
        return new IfNode(cond, block, elseBlock);
    }

    private StatementNode parseWhileStatement() {
        if (!match("OUV_PAREN")) { error("'(' attendu", peek()); return null; }
        ExpressionNode cond = parseExpression();
        if (!match("FER_PAREN")) { error("')' attendu", peek()); return null; }
        if (!match("CROCH_OUV")) { error("'{' attendu", peek()); return null; }
        List<StatementNode> block = parseBlock();
        return new WhileNode(cond, block);
    }

    private StatementNode parseForStatement() {
        if (!match("OUV_PAREN")) { error("'(' attendu", peek()); return null; }
        StatementNode init = parseStatement();
        ExpressionNode cond = parseExpression();
        if (!match("SEMICOL")) error("';' attendu dans for", peek());
        Token idToken = consume();

        StatementNode incr = null;

        if (peek() != null && peek().getType().equals("INC")) {
            consume();
            incr = new UnaryStatementNode(idToken.getValue(), "++", false);
        }
        else if (peek() != null && peek().getType().equals("DEC")) {
            consume();
            incr = new UnaryStatementNode(idToken.getValue(), "--", false);
        }
        else if (peek() != null && peek().getType().equals("AFFECT")) {
            consume();
            ExpressionNode expr = parseExpression();
            incr = new AssignmentNode(idToken.getValue(), expr);
        }
        if (!match("FER_PAREN")) { error("')' attendu", peek()); return null; }
        if (!match("CROCH_OUV")) { error("'{' attendu", peek()); return null; }
        List<StatementNode> block = parseBlock();
        return new ForNode(init, cond, incr, block);
    }

    private StatementNode parseDoWhileStatement() {

        if (!match("CROCH_OUV")) {
            error("'{' attendu", peek());
            return null;
        }

        List<StatementNode> block = parseBlock();

        if (!match("WHILE")) {
            error("'while' attendu après do", peek());
            return null;
        }

        if (!match("OUV_PAREN")) {
            error("'(' attendu", peek());
            return null;
        }

        ExpressionNode cond = parseExpression();

        if (!match("FER_PAREN")) {
            error("')' attendu", peek());
            return null;
        }

        if (!match("SEMICOL")) {
            error("';' attendu après do-while", peek());
        }

        return new DoWhileNode(cond, block);
    }

    private List<StatementNode> parseBlock() {
        List<StatementNode> block = new ArrayList<>();
        while (!match("CROCH_FER") && peek() != null) {
            StatementNode stmt = parseStatement();
            if (stmt != null) block.add(stmt);
        }
        return block;
    }

    // ================== Switch ==================
    private StatementNode parseSwitchStatement() {
        consume(); // SWITCH
        if (!match("OUV_PAREN")) { error("'(' attendu", peek()); return null; }
        ExpressionNode expr = parseExpression();
        if (!match("FER_PAREN")) { error("')' attendu", peek()); return null; }
        if (!match("CROCH_OUV")) { error("'{' attendu", peek()); return null; }

        List<CaseNode> cases = new ArrayList<>();
        while (peek() != null && !peek().getType().equals("CROCH_FER")) {
            if (!match("CASE")) { error("'case' attendu", peek()); break; }
            ExpressionNode caseExpr = parseExpression();
            if (!match("COLON")) { error("':' attendu après case", peek()); break; }

            List<StatementNode> caseBlock = new ArrayList<>();
            while (peek() != null &&
                    !peek().getType().equals("CASE") &&
                    !peek().getType().equals("CROCH_FER")) {
                StatementNode stmt = parseStatement();
                if (stmt != null) caseBlock.add(stmt);
            }
            cases.add(new CaseNode(caseExpr, caseBlock));
        }
        match("CROCH_FER"); // ferme le switch
        return new SwitchNode(expr, cases);
    }
}