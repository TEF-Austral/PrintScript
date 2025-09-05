package builder

import Token
import node.Program
import node.BinaryExpression
import node.IdentifierExpression
import node.LiteralExpression
import node.AssignmentStatement
import node.DeclarationStatement
import node.EmptyStatement
import node.Expression
import node.ExpressionStatement
import node.IfStatement
import node.PrintStatement
import node.Statement

class DefaultNodeBuilder : NodeBuilder {
    override fun buildLiteralExpressionNode(token: Token): LiteralExpression = LiteralExpression(token)

    override fun buildIdentifierNode(token: Token): IdentifierExpression = IdentifierExpression(token)

    override fun buildBinaryExpressionNode(
        left: Expression,
        operator: Token,
        right: Expression,
    ): BinaryExpression = BinaryExpression(left, operator, right)

    override fun buildVariableDeclarationStatementNode(
        identifier: Token,
        dataType: Token,
        initialValue: Expression?,
    ): DeclarationStatement = DeclarationStatement(identifier, dataType, initialValue)

    override fun buildAssignmentStatementNode(
        identifier: Token,
        value: Expression,
    ): AssignmentStatement = AssignmentStatement(identifier, value)

    override fun buildPrintStatementNode(expression: Expression): PrintStatement = PrintStatement(expression)

    override fun buildExpressionStatementNode(expression: Expression): ExpressionStatement = ExpressionStatement(expression)

    override fun buildEmptyStatementNode(): EmptyStatement = EmptyStatement()

    override fun buildProgramNode(statements: List<Statement>): Program = Program(statements)

    override fun buildIfStatementNode(
        condition: Expression,
        consequence: Statement,
        alternative: Statement?,
    ): IfStatement = IfStatement(condition, consequence, alternative)
}
