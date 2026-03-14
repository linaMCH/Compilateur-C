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
                // Pour l’instant on ne gère que les déclarations
                return null;
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
            Token next = position < tokens.size() ? tokens.get(position) : null;
            error("';' attendu", next);
            return null;
        }

        // Utilisation de la classe concrète VariableDeclarationNode
        return new VariableDeclarationNode(typeToken.getValue(), idToken.getValue());
    }
}