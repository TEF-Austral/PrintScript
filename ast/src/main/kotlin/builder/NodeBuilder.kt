package builder

import Token
import node.Program
import node.expression.Expression
import node.statement.Statement

sealed interface NodeBuilder {

    fun buildLiteralExpressionNode(token: Token): Expression

    fun buildIdentifierNode(token: Token): Expression

    fun buildBinaryExpressionNode(left: Expression, operator: Token, right: Expression): Expression

    fun buildVariableDeclarationStatementNode(
        identifier: Token,
        dataType: Token,
        initialValue: Expression? = null
    ): Statement

    fun buildAssignmentStatementNode(identifier: Token, value: Expression): Statement

    fun buildPrintStatementNode(expression: Expression): Statement

    fun buildExpressionStatementNode(expression: Expression): Statement

    fun buildEmptyStatementNode(): Statement

    fun buildProgramNode(statements: List<Statement>): Program
}