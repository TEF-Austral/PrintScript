package builder

import Token
import coordinates.Coordinates
import node.Expression
import node.IfStatement
import node.Program
import node.Statement

sealed interface NodeBuilder {
    fun buildLiteralExpressionNode(token: Token, coordinates: Coordinates): Expression

    fun buildIdentifierNode(token: Token, coordinates: Coordinates): Expression

    fun buildBinaryExpressionNode(
        left: Expression,
        operator: Token,
        right: Expression,
        coordinates: Coordinates
    ): Expression

    fun buildVariableDeclarationStatementNode(
        declarationType: Token,
        identifier: Token,
        dataType: Token,
        initialValue: Expression? = null,
        coordinates: Coordinates
    ): Statement

    fun buildAssignmentStatementNode(
        identifier: Token,
        value: Expression,
        coordinates: Coordinates
    ): Statement

    fun buildPrintStatementNode(expression: Expression, coordinates: Coordinates): Statement

    fun buildExpressionStatementNode(expression: Expression, coordinates: Coordinates): Statement

    fun buildEmptyStatementNode(): Statement

    fun buildProgramNode(statements: List<Statement>): Program

    fun buildIfStatementNode(
        condition: Expression,
        consequence: Statement,
        alternative: Statement? = null,
        coordinates: Coordinates
    ): IfStatement
}
