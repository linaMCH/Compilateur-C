package com.example.compil;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class LexerRunner {

    public static List<Token> tokenize(String code) {

        List<Token> tokens = new ArrayList<>();

        try {

            Analyseur_lex lexer = new Analyseur_lex(
                    new StringReader(code)
            );

            Token token;

            while ((token = lexer.yylex()) != null) {
                tokens.add(token);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tokens;
    }
}