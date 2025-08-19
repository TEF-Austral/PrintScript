package builder

import Token
import node.Program
import node.expression.BinaryExpression
import node.expression.Expression
import node.expression.IdentifierExpression
import node.expression.LiteralExpression
import node.statement.AssignmentStatement
import node.statement.DeclarationStatement
import node.statement.EmptyStatement
import node.statement.ExpressionStatement
import node.statement.PrintStatement
import node.statement.Statement

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
    ): DeclarationStatement {
        return DeclarationStatement(identifier, dataType, initialValue)
    }

    override fun buildAssignmentStatementNode(identifier: Token, value: Expression): AssignmentStatement {
        return AssignmentStatement(identifier, value)
    }

    override fun buildPrintStatementNode(expression: Expression): PrintStatement {
        return PrintStatement(expression)
    }

    override fun buildExpressionStatementNode(expression: Expression): ExpressionStatement {
        return ExpressionStatement(expression)
    }

    override fun buildEmptyStatementNode(): EmptyStatement {
        return EmptyStatement()
    }

    override fun buildProgramNode(statements: List<Statement>): Program {
        return Program(statements)
    }
}