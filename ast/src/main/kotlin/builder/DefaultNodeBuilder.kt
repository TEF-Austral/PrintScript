package builder

import Token
import coordinates.Coordinates
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
    override fun buildLiteralExpressionNode(token: Token, coordinates: Coordinates): LiteralExpression = LiteralExpression(token, coordinates)

    override fun buildIdentifierNode(token: Token, coordinates: Coordinates): IdentifierExpression = IdentifierExpression(token, coordinates)

    override fun buildBinaryExpressionNode(
        left: Expression,
        operator: Token,
        right: Expression,
        coordinates: Coordinates
    ): BinaryExpression = BinaryExpression(left, operator, right, coordinates)

    override fun buildVariableDeclarationStatementNode(
        declarationType: Token,
        identifier: Token,
        dataType: Token,
        initialValue: Expression?,
        coordinates: Coordinates
    ): DeclarationStatement = DeclarationStatement(declarationType, identifier, dataType, initialValue, coordinates)

    override fun buildAssignmentStatementNode(
        identifier: Token,
        value: Expression,
        coordinates: Coordinates
    ): AssignmentStatement = AssignmentStatement(identifier, value, coordinates)

    override fun buildPrintStatementNode(expression: Expression, coordinates: Coordinates): PrintStatement = PrintStatement(expression, coordinates)

    override fun buildExpressionStatementNode(expression: Expression, coordinates: Coordinates): ExpressionStatement = ExpressionStatement(expression, coordinates)

    override fun buildEmptyStatementNode(): EmptyStatement = EmptyStatement()

    override fun buildProgramNode(statements: List<Statement>): Program = Program(statements)

    override fun buildIfStatementNode(
        condition: Expression,
        consequence: Statement,
        alternative: Statement?,
        coordinates: Coordinates
    ): IfStatement = IfStatement(condition, consequence, alternative, coordinates)
}
