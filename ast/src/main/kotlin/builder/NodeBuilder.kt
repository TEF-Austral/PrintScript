package builder

import Token
import node.Expression
import node.IfStatement
import node.LiteralExpression
import node.Program
import node.ReadEnvStatement
import node.ReadInputStatement
import node.Statement

sealed interface NodeBuilder {
    fun buildLiteralExpressionNode(token: Token): Expression

    fun buildIdentifierNode(token: Token): Expression

    fun buildBinaryExpressionNode(
        left: Expression,
        operator: Token,
        right: Expression,
    ): Expression

    fun buildVariableDeclarationStatementNode(
        declarationType: Token,
        identifier: Token,
        dataType: Token,
        initialValue: Expression? = null,
    ): Statement

    fun buildAssignmentStatementNode(
        identifier: Token,
        value: Expression,
    ): Statement

    fun buildPrintStatementNode(expression: Expression): Statement

    fun buildExpressionStatementNode(expression: Expression): Statement

    fun buildEmptyStatementNode(): Statement

    fun buildProgramNode(statements: List<Statement>): Program

    fun buildIfStatementNode(
        condition: Expression,
        consequence: Statement,
        alternative: Statement? = null,
    ): IfStatement

    fun buildReadInputNode(printValue: LiteralExpression): ReadInputStatement

    fun buildReadEnvNode(envName: LiteralExpression): ReadEnvStatement
}
