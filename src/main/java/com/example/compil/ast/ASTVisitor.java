package com.example.compil.ast;

public interface ASTVisitor {
    Object visit(BinaryExpressionNode node);
    Object visit(AssignmentNode node);
    Object visit(LiteralNode node);
    Object visit(VariableNode node);
    Object visit(IfNode node);
    Object visit(WhileNode node);
    Object visit(CaseNode node);
    Object visit (ExpressionNode node);
    Object visit (ForNode node);
    Object visit (ReturnNode node);
    Object visit (SwitchNode node);
    Object visit (UnaryExpressionNode node);
    Object visit (VariableDeclarationNode node);
    Object visit (DoWhileNode node);
    Object visit (UnaryStatementNode node);

    // On ajoutera les autres au fur et à mesure
}