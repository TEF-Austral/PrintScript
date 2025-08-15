package builder

import Token
import node.AssignmentStatement
import node.BinaryExpression
import node.BlockStatement
import node.Expression
import node.ExpressionStatement
import node.IdentifierExpression
import node.LiteralExpression
import node.PrintStatement
import node.Program
import node.Statement
import node.VariableDeclarationStatement

sealed interface NodeBuilder {

    fun buildLiteralExpressionNode(token: Token): LiteralExpression

    fun buildIdentifierNode(token: Token): IdentifierExpression

    fun buildBinaryExpressionNode(left: Expression, operator: Token, right: Expression): BinaryExpression

    fun buildVariableDeclarationStatementNode(
        identifier: Token,
        dataType: Token,
        initialValue: Expression? = null
    ): VariableDeclarationStatement

    fun buildAssignmentStatementNode(identifier: Token, value: Expression): AssignmentStatement

    fun buildBlockStatementNode(statements: List<Statement>): BlockStatement

    fun buildPrintStatementNode(expression: Expression): PrintStatement

    fun buildExpressionStatementNode(expression: Expression): ExpressionStatement

    fun buildProgramNode(statements: List<Statement>): Program

}

class DefaultNodeBuilder : NodeBuilder {

    override fun buildLiteralExpressionNode(token: Token): LiteralExpression {
        return LiteralExpression(token)
    }

    override fun buildIdentifierNode(token: Token): IdentifierExpression {
        return IdentifierExpression(token)
    }

    override fun buildBinaryExpressionNode(
        left: Expression,
        operator: Token,
        right: Expression
    ): BinaryExpression {
        return BinaryExpression(left, operator, right)
    }

    override fun buildVariableDeclarationStatementNode(
        identifier: Token,
        dataType: Token,
        initialValue: Expression?
    ): VariableDeclarationStatement {
        return VariableDeclarationStatement(identifier, dataType, initialValue)
    }

    override fun buildAssignmentStatementNode(identifier: Token, value: Expression): AssignmentStatement {
        return AssignmentStatement(identifier, value)
    }

    override fun buildBlockStatementNode(statements: List<Statement>): BlockStatement {
        return BlockStatement(statements)
    }

    override fun buildPrintStatementNode(expression: Expression): PrintStatement {
        return PrintStatement(expression)
    }

    override fun buildExpressionStatementNode(expression: Expression): ExpressionStatement {
        return ExpressionStatement(expression)
    }

    override fun buildProgramNode(statements: List<Statement>): Program {
        return Program(statements)
    }
}