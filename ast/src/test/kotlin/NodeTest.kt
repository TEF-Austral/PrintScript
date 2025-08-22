package node

import Position
import PrintScriptToken
import Token
import builder.DefaultNodeBuilder
import node.expression.BinaryExpression
import node.expression.EmptyExpression
import node.expression.LiteralExpression
import node.statement.DeclarationStatement
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NodeTest {
    private lateinit var nodeBuilder: DefaultNodeBuilder
    private lateinit var mockToken: Token
    private lateinit var numberToken: Token
    private lateinit var identifierToken: Token
    private lateinit var operatorToken: Token
    private lateinit var dataTypeToken: Token

    @BeforeEach
    fun setUp() {
        nodeBuilder = DefaultNodeBuilder()

        // Create mock tokens for testing
        val coordinates = Position(1, 1)
        mockToken = PrintScriptToken(TokenType.STRING_LITERAL, "test", coordinates)
        numberToken = PrintScriptToken(TokenType.NUMBER_LITERAL, "42", coordinates)
        identifierToken = PrintScriptToken(TokenType.IDENTIFIER, "myVar", coordinates)
        operatorToken = PrintScriptToken(TokenType.OPERATORS, "+", coordinates)
        dataTypeToken = PrintScriptToken(TokenType.DATA_TYPES, "NUMBER", coordinates)
    }

    @Test
    fun testLiteralExpression() {
        val literalExpr = nodeBuilder.buildLiteralExpressionNode(mockToken)

        assertEquals("test", literalExpr.getValue())
        assertNotNull(literalExpr)
    }

    @Test
    fun testIdentifierExpression() {
        val identifierExpr = nodeBuilder.buildIdentifierNode(identifierToken)

        assertEquals("myVar", identifierExpr.getName())
        assertNotNull(identifierExpr)
    }

    @Test
    fun testBinaryExpression() {
        val leftExpr = nodeBuilder.buildLiteralExpressionNode(numberToken)
        val rightExpr = nodeBuilder.buildIdentifierNode(identifierToken)

        val binaryExpr = nodeBuilder.buildBinaryExpressionNode(leftExpr, operatorToken, rightExpr)

        assertEquals(leftExpr, binaryExpr.getLeft())
        assertEquals(operatorToken, binaryExpr.getOperator())
        assertEquals(rightExpr, binaryExpr.getRight())
    }

    @Test
    fun testVariableDeclarationStatement() {
        val initialValue = nodeBuilder.buildLiteralExpressionNode(numberToken)

        val varDecl =
            nodeBuilder.buildVariableDeclarationStatementNode(
                identifierToken,
                dataTypeToken,
                initialValue,
            )

        assertEquals("myVar", varDecl.getIdentifier())
        assertEquals("NUMBER", varDecl.getDataType())
        assertEquals(initialValue, varDecl.getInitialValue())
    }

    @Test
    fun testVariableDeclarationStatementWithoutInitialValue() {
        val varDecl =
            nodeBuilder.buildVariableDeclarationStatementNode(
                identifierToken,
                dataTypeToken,
            )

        assertEquals("myVar", varDecl.getIdentifier())
        assertEquals("NUMBER", varDecl.getDataType())
        assertNull(varDecl.getInitialValue())
    }

    @Test
    fun testAssignmentStatement() {
        val value = nodeBuilder.buildLiteralExpressionNode(numberToken)

        val assignment = nodeBuilder.buildAssignmentStatementNode(identifierToken, value)

        assertEquals("myVar", assignment.getIdentifier())
        assertEquals(value, assignment.getValue())
    }

    @Test
    fun testPrintStatement() {
        val expression = nodeBuilder.buildLiteralExpressionNode(mockToken)

        val printStmt = nodeBuilder.buildPrintStatementNode(expression)

        assertEquals(expression, printStmt.getExpression())
    }

    @Test
    fun testExpressionStatement() {
        val expression = nodeBuilder.buildIdentifierNode(identifierToken)

        val exprStmt = nodeBuilder.buildExpressionStatementNode(expression)

        assertEquals(expression, exprStmt.getExpression())
    }

    @Test
    fun testEmptyStatement() {
        val emptyStmt = nodeBuilder.buildEmptyStatementNode()

        assertNotNull(emptyStmt)
        assertEquals("EmptyStatement", emptyStmt.toString())
    }

    @Test
    fun testProgram() {
        val stmt1 = nodeBuilder.buildEmptyStatementNode()
        val stmt2 =
            nodeBuilder.buildPrintStatementNode(
                nodeBuilder.buildLiteralExpressionNode(mockToken),
            )
        val statements = listOf(stmt1, stmt2)

        val program = nodeBuilder.buildProgramNode(statements)

        assertEquals(2, program.getStatements().size)
        assertEquals(statements, program.getStatements())
    }

    @Test
    fun testEmptyExpression() {
        val emptyExpr = EmptyExpression()

        assertEquals("EmptyExpression", emptyExpr.toString())
    }

    @Test
    fun testComplexAST() {
        // Build a more complex AST: let x: NUMBER = 5 + 10;
        val leftOperand =
            nodeBuilder.buildLiteralExpressionNode(
                PrintScriptToken(TokenType.NUMBER_LITERAL, "5", Position(1, 15)),
            )
        val rightOperand =
            nodeBuilder.buildLiteralExpressionNode(
                PrintScriptToken(TokenType.NUMBER_LITERAL, "10", Position(1, 19)),
            )
        val binaryExpr = nodeBuilder.buildBinaryExpressionNode(leftOperand, operatorToken, rightOperand)

        val varDecl =
            nodeBuilder.buildVariableDeclarationStatementNode(
                PrintScriptToken(TokenType.IDENTIFIER, "x", Position(1, 5)),
                PrintScriptToken(TokenType.DATA_TYPES, "NUMBER", Position(1, 8)),
                binaryExpr,
            )

        val program = nodeBuilder.buildProgramNode(listOf(varDecl))

        assertEquals(1, program.getStatements().size)
        val statement = program.getStatements()[0] as DeclarationStatement
        assertEquals("x", statement.getIdentifier())
        assertEquals("NUMBER", statement.getDataType())
        assertNotNull(statement.getInitialValue())

        val initialValueBinary = statement.getInitialValue() as BinaryExpression
        assertEquals("5", (initialValueBinary.getLeft() as LiteralExpression).getValue())
        assertEquals("+", initialValueBinary.getOperator().getValue())
        assertEquals("10", (initialValueBinary.getRight() as LiteralExpression).getValue())
    }
}
