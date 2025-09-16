// kotlin
package builder

import Token
import node.Expression
import node.IfStatement
import node.LiteralExpression
import node.Program
import node.ReadEnvExpression
import node.ReadInputExpression
import node.Statement

sealed interface NodeBuilder {
    fun buildLiteralExpressionNode(token: Token): Expression

    fun buildIdentifierNode(token: Token): Expression

    fun buildBinaryExpressionNode(
        left: Expression,
        operator: Token,
        right: Expression,
        operatorWithSpacing: Token? = null, // raw like " + " or "+ "
    ): Expression

    fun buildVariableDeclarationStatementNode(
        declarationType: Token,
        identifier: Token,
        dataType: Token,
        initialValue: Expression? = null,
        assignmentOperator: Token? = null, // raw like " = "
        colonToken: Token? = null, // raw like ": " or " :"
    ): Statement

    fun buildAssignmentStatementNode(
        identifier: Token,
        value: Expression,
        assignmentOperator: Token? = null, // raw like " ="
    ): Statement

    fun buildPrintStatementNode(expression: Expression): Statement

    fun buildExpressionStatementNode(expression: Expression): Statement

    fun buildEmptyStatementNode(): Statement

    fun buildProgramNode(statements: List<Statement>): Program

    fun buildIfStatementNode(
        condition: Expression,
        consequence: Statement,
        alternative: Statement? = null,
        ifHeaderToken: Token? = null, // e.g., "if(" or "if ("
        closeParenToken: Token? = null, // e.g., ") " or ")"
        elseTokenRaw: Token? = null, // e.g., " else " or "\nelse "
        bracesOnSameLine: Boolean? = null, // true if original had ") {"
    ): IfStatement

    fun buildReadInputNode(printValue: LiteralExpression): ReadInputExpression

    fun buildReadEnvNode(envName: LiteralExpression): ReadEnvExpression
}
