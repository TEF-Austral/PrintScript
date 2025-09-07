package node

import coordinates.Position
import PrintScriptToken
import Token
import builder.DefaultNodeBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import type.CommonTypes
import kotlin.toString

class NodeTest {
    private lateinit var nodeBuilder: DefaultNodeBuilder
    private lateinit var mockToken: Token
    private lateinit var numberToken: Token
    private lateinit var identifierToken: Token
    private lateinit var operatorToken: Token
    private lateinit var dataTypeToken: Token
    private lateinit var declarationTypeToken: Token

    @BeforeEach
    fun setUp() {
        nodeBuilder = DefaultNodeBuilder()

        // Create mock tokens for testing
        val coordinates = Position(1, 1)
        mockToken = PrintScriptToken(CommonTypes.STRING_LITERAL, "test", coordinates)
        numberToken = PrintScriptToken(CommonTypes.NUMBER_LITERAL, "42", coordinates)
        identifierToken = PrintScriptToken(CommonTypes.IDENTIFIER, "myVar", coordinates)
        operatorToken = PrintScriptToken(CommonTypes.OPERATORS, "+", coordinates)
        dataTypeToken = PrintScriptToken(CommonTypes.NUMBER, "NUMBER", coordinates)
        declarationTypeToken = PrintScriptToken(CommonTypes.LET, "let", coordinates)
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

        assertEquals("myVar", identifierExpr.getValue())
        assertNotNull(identifierExpr)
    }

    @Test
    fun testBinaryExpression() {
        val leftExpr = nodeBuilder.buildLiteralExpressionNode(numberToken)
        val rightExpr = nodeBuilder.buildIdentifierNode(identifierToken)

        val binaryExpr = nodeBuilder.buildBinaryExpressionNode(leftExpr, operatorToken, rightExpr)

        assertEquals(leftExpr, binaryExpr.getLeft())
        assertEquals("+", binaryExpr.getOperator())
        assertEquals(rightExpr, binaryExpr.getRight())
    }

    @Test
    fun testVariableDeclarationStatement() {
        val initialValue = nodeBuilder.buildLiteralExpressionNode(numberToken)

        val varDecl =
            nodeBuilder.buildVariableDeclarationStatementNode(
                declarationTypeToken,
                identifierToken,
                dataTypeToken,
                initialValue,
            )

        assertEquals("myVar", varDecl.getIdentifier())
        assertEquals(CommonTypes.NUMBER, varDecl.getDataType())
        assertEquals(initialValue, varDecl.getInitialValue())
    }

    @Test
    fun testVariableDeclarationStatementWithoutInitialValue() {
        val varDecl =
            nodeBuilder.buildVariableDeclarationStatementNode(
                declarationTypeToken,
                identifierToken,
                dataTypeToken,
            )

        assertEquals("myVar", varDecl.getIdentifier())
        assertEquals(CommonTypes.NUMBER, varDecl.getDataType())
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
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 15)),
            )
        val rightOperand =
            nodeBuilder.buildLiteralExpressionNode(
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "10", Position(1, 19)),
            )
        val binaryExpr = nodeBuilder.buildBinaryExpressionNode(leftOperand, operatorToken, rightOperand)

        val varDecl =
            nodeBuilder.buildVariableDeclarationStatementNode(
                declarationTypeToken,
                PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
                PrintScriptToken(CommonTypes.NUMBER, "NUMBER", Position(1, 8)),
                binaryExpr,
            )

        val program = nodeBuilder.buildProgramNode(listOf(varDecl))

        assertEquals(1, program.getStatements().size)
        val statement = program.getStatements()[0] as DeclarationStatement
        assertEquals("x", statement.getIdentifier())
        assertEquals(CommonTypes.NUMBER, statement.getDataType())
        assertNotNull(statement.getInitialValue())

        val initialValueBinary = statement.getInitialValue() as BinaryExpression
        assertEquals("5", (initialValueBinary.getLeft() as LiteralExpression).getValue())
        assertEquals("+", initialValueBinary.getOperator())
        assertEquals("10", (initialValueBinary.getRight() as LiteralExpression).getValue())
    }

    @Test
    fun `Simple If Statement with Nodebuilder`() {
        val condition = nodeBuilder.buildLiteralExpressionNode(mockToken)
        val thenBranch = nodeBuilder.buildEmptyStatementNode()
        val elseBranch = nodeBuilder.buildEmptyStatementNode()
        val ifStatement = nodeBuilder.buildIfStatementNode(condition, thenBranch, elseBranch)
        assertEquals(condition, ifStatement.getCondition())
        assertEquals(thenBranch, ifStatement.getConsequence())
        assertEquals(elseBranch, ifStatement.getAlternative())
        assertEquals(true, ifStatement.hasAlternative())
    }

    @Test
    fun `Simple If Statement`() {
        val condition = nodeBuilder.buildLiteralExpressionNode(mockToken)
        val thenBranch = nodeBuilder.buildEmptyStatementNode()
        val elseBranch = nodeBuilder.buildEmptyStatementNode()
        val ifStatement = IfStatement(condition, thenBranch, elseBranch, Position(1, 1))
        assertEquals(condition, ifStatement.getCondition())
        assertEquals(thenBranch, ifStatement.getConsequence())
        assertEquals(elseBranch, ifStatement.getAlternative())
        assertEquals(true, ifStatement.hasAlternative())
    }

    @Test
    fun `Simple If Statement Without Else`() {
        val condition = nodeBuilder.buildLiteralExpressionNode(mockToken)
        val thenBranch = nodeBuilder.buildEmptyStatementNode()
        val ifStatement = IfStatement(condition, thenBranch, null, Position(1, 1))
        assertEquals(condition, ifStatement.getCondition())
        assertEquals(thenBranch, ifStatement.getConsequence())
        assertEquals(null, ifStatement.getAlternative())
        assertEquals(false, ifStatement.hasAlternative())
    }

    @Test
    fun testAssignmentStatementGetIdentifierToken() {
        val value = nodeBuilder.buildLiteralExpressionNode(numberToken)
        val assignment = nodeBuilder.buildAssignmentStatementNode(identifierToken, value)

        assertEquals(identifierToken, assignment.getIdentifierToken())
    }

    @Test
    fun testPrintStatementGetExpressionTokenWithIdentifier() {
        val identifierExpr = nodeBuilder.buildIdentifierNode(identifierToken)
        val printStmt = nodeBuilder.buildPrintStatementNode(identifierExpr)

        assertEquals(identifierToken, printStmt.getExpressionToken())
    }

    @Test
    fun testPrintStatementGetExpressionTokenWithNonIdentifier() {
        val literalExpr = nodeBuilder.buildLiteralExpressionNode(mockToken)
        val printStmt = nodeBuilder.buildPrintStatementNode(literalExpr)

        assertNull(printStmt.getExpressionToken())
    }

    @Test
    fun testIfStatementGetAlternativeWhenNull() {
        val condition = nodeBuilder.buildLiteralExpressionNode(mockToken)
        val thenBranch = nodeBuilder.buildEmptyStatementNode()
        val ifStatement = nodeBuilder.buildIfStatementNode(condition, thenBranch, null)

        assertNull(ifStatement.getAlternative())
    }

    @Test
    fun testEmptyExpressionToString() {
        val emptyExpr = EmptyExpression()

        assertEquals("EmptyExpression", emptyExpr.toString())
    }

    @Test
    fun testLiteralExpressionGetType() {
        val literalExpr = nodeBuilder.buildLiteralExpressionNode(numberToken)

        assertEquals(CommonTypes.NUMBER_LITERAL, literalExpr.getType())
    }

    @Test
    fun testProgramWithNoStatements() {
        val program = nodeBuilder.buildProgramNode(emptyList())

        assertEquals(0, program.getStatements().size)
    }
}
