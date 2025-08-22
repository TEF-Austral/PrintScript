import builder.DefaultNodeBuilder
import node.expression.BinaryExpression
import node.expression.IdentifierExpression
import node.expression.LiteralExpression
import node.statement.DeclarationStatement
import node.statement.ExpressionStatement
import parser.factory.RecursiveParserFactory
import parser.result.ParseResult
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class ParserTest {

    private fun parseProgram(tokens: List<PrintScriptToken>) =
        when (val result = RecursiveParserFactory()
            .createParser(tokens, DefaultNodeBuilder())
            .parse()
        ) {
            is ParseResult.Success -> result.value
            is ParseResult.Failure ->
                fail("Parsing failed: ${result.error.message} at ${result.error.position}")
        }

    @Test
    fun testSingleVariableDeclaration() {
        val tokens = listOf(
            PrintScriptToken(TokenType.DECLARATION, "let", Position(1, 1)),
            PrintScriptToken(TokenType.IDENTIFIER, "x", Position(1, 5)),
            PrintScriptToken(TokenType.DELIMITERS, ":", Position(1, 7)),
            PrintScriptToken(TokenType.DATA_TYPES, "NUMBER", Position(1, 9)),
            PrintScriptToken(TokenType.ASSIGNMENT, "=", Position(1, 16)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "5", Position(1, 18)),
            PrintScriptToken(TokenType.DELIMITERS, ";", Position(1, 19))
        )

        val program = parseProgram(tokens)
        assertEquals(1, program.getStatements().size)
        val statement = program.getStatements()[0]
        assertTrue(statement is DeclarationStatement)

        val varDecl = statement as DeclarationStatement
        assertEquals("x", varDecl.getIdentifier())
        assertEquals("NUMBER", varDecl.getDataType())
        assertTrue(varDecl.getInitialValue() is LiteralExpression)

        val literal = varDecl.getInitialValue() as LiteralExpression
        assertEquals("5", literal.getValue())
    }

    @Test
    fun testMultipleStatementsWithBinaryExpression() {
        val tokens = listOf(
            PrintScriptToken(TokenType.DECLARATION, "let", Position(1, 1)),
            PrintScriptToken(TokenType.IDENTIFIER, "x", Position(1, 5)),
            PrintScriptToken(TokenType.DELIMITERS, ":", Position(1, 7)),
            PrintScriptToken(TokenType.DATA_TYPES, "NUMBER", Position(1, 9)),
            PrintScriptToken(TokenType.ASSIGNMENT, "=", Position(1, 16)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "5", Position(1, 18)),
            PrintScriptToken(TokenType.DELIMITERS, ";", Position(1, 19)),

            PrintScriptToken(TokenType.DECLARATION, "let", Position(2, 1)),
            PrintScriptToken(TokenType.IDENTIFIER, "y", Position(2, 5)),
            PrintScriptToken(TokenType.DELIMITERS, ":", Position(2, 7)),
            PrintScriptToken(TokenType.DATA_TYPES, "NUMBER", Position(2, 9)),
            PrintScriptToken(TokenType.ASSIGNMENT, "=", Position(2, 16)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "5", Position(2, 18)),
            PrintScriptToken(TokenType.DELIMITERS, ";", Position(2, 19)),

            PrintScriptToken(TokenType.IDENTIFIER, "x", Position(3, 1)),
            PrintScriptToken(TokenType.OPERATORS, "+", Position(3, 2)),
            PrintScriptToken(TokenType.IDENTIFIER, "y", Position(3, 3)),
            PrintScriptToken(TokenType.DELIMITERS, ";", Position(3, 4))
        )

        val program = parseProgram(tokens)
        assertEquals(3, program.getStatements().size)

        val firstVarDecl = program.getStatements()[0] as DeclarationStatement
        assertEquals("x", firstVarDecl.getIdentifier())
        assertEquals("NUMBER", firstVarDecl.getDataType())
        assertEquals("5", (firstVarDecl.getInitialValue() as LiteralExpression).getValue())

        val secondVarDecl = program.getStatements()[1] as DeclarationStatement
        assertEquals("y", secondVarDecl.getIdentifier())
        assertEquals("NUMBER", secondVarDecl.getDataType())
        assertEquals("5", (secondVarDecl.getInitialValue() as LiteralExpression).getValue())

        val exprStatement = program.getStatements()[2] as ExpressionStatement
        val binaryExpr = exprStatement.getExpression() as BinaryExpression
        assertTrue(binaryExpr.getLeft() is IdentifierExpression)
        assertTrue(binaryExpr.getRight() is IdentifierExpression)
        assertEquals("+", binaryExpr.getOperator().getValue())

        val leftIdentifier = binaryExpr.getLeft() as IdentifierExpression
        val rightIdentifier = binaryExpr.getRight() as IdentifierExpression
        assertEquals("x", leftIdentifier.getName())
        assertEquals("y", rightIdentifier.getName())
    }

    @Test
    fun testComplexExpressionWithParenthesesAndPrecedence() {
        val tokens = listOf(
            PrintScriptToken(TokenType.DELIMITERS, "(", Position(1, 1)),
            PrintScriptToken(TokenType.IDENTIFIER, "x", Position(1, 2)),
            PrintScriptToken(TokenType.OPERATORS, "+", Position(1, 4)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "5", Position(1, 6)),
            PrintScriptToken(TokenType.DELIMITERS, ")", Position(1, 7)),
            PrintScriptToken(TokenType.OPERATORS, "*", Position(1, 9)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "2", Position(1, 11)),
            PrintScriptToken(TokenType.COMPARISON, ">", Position(1, 13)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "10", Position(1, 15)),
            PrintScriptToken(TokenType.DELIMITERS, ";", Position(1, 17))
        )

        val program = parseProgram(tokens)
        assertEquals(1, program.getStatements().size)
        val exprStatement = program.getStatements()[0] as ExpressionStatement
        val rootBinary = exprStatement.getExpression() as BinaryExpression

        assertEquals(">", rootBinary.getOperator().getValue())
        assertTrue(rootBinary.getRight() is LiteralExpression)
        assertEquals("10", (rootBinary.getRight() as LiteralExpression).getValue())

        val leftMultiplication = rootBinary.getLeft() as BinaryExpression
        assertEquals("*", leftMultiplication.getOperator().getValue())
        assertEquals("2", (leftMultiplication.getRight() as LiteralExpression).getValue())

        val addition = leftMultiplication.getLeft() as BinaryExpression
        assertEquals("+", addition.getOperator().getValue())
        assertEquals("x", (addition.getLeft() as IdentifierExpression).getName())
        assertEquals("5", (addition.getRight() as LiteralExpression).getValue())
    }

    @Test
    fun testExpressionWithMultiplePrecedenceLevels() {
        val tokens = listOf(
            PrintScriptToken(TokenType.NUMBER_LITERAL, "2", Position(1, 1)),
            PrintScriptToken(TokenType.OPERATORS, "+", Position(1, 3)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "3", Position(1, 5)),
            PrintScriptToken(TokenType.OPERATORS, "*", Position(1, 7)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "4", Position(1, 9)),
            PrintScriptToken(TokenType.COMPARISON, ">", Position(1, 11)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "10", Position(1, 14)),
            PrintScriptToken(TokenType.DELIMITERS, ";", Position(1, 16))
        )

        val program = parseProgram(tokens)
        assertEquals(1, program.getStatements().size)
        val rootBinary = (program.getStatements()[0] as ExpressionStatement).getExpression() as BinaryExpression

        assertEquals(">", rootBinary.getOperator().getValue())
        val leftAddition = rootBinary.getLeft() as BinaryExpression
        assertEquals("+", leftAddition.getOperator().getValue())
        val multiplication = leftAddition.getRight() as BinaryExpression
        assertEquals("*", multiplication.getOperator().getValue())

        assertEquals("2", (leftAddition.getLeft() as LiteralExpression).getValue())
        assertEquals("3", (multiplication.getLeft() as LiteralExpression).getValue())
        assertEquals("4", (multiplication.getRight() as LiteralExpression).getValue())
        assertEquals("10", (rootBinary.getRight() as LiteralExpression).getValue())
    }

    @Test
    fun testNestedParenthesesExpression() {
        val tokens = listOf(
            PrintScriptToken(TokenType.DELIMITERS, "(", Position(1, 1)),
            PrintScriptToken(TokenType.DELIMITERS, "(", Position(1, 2)),
            PrintScriptToken(TokenType.IDENTIFIER, "x", Position(1, 3)),
            PrintScriptToken(TokenType.OPERATORS, "+", Position(1, 5)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "1", Position(1, 7)),
            PrintScriptToken(TokenType.DELIMITERS, ")", Position(1, 8)),
            PrintScriptToken(TokenType.OPERATORS, "*", Position(1, 10)),
            PrintScriptToken(TokenType.DELIMITERS, "(", Position(1, 12)),
            PrintScriptToken(TokenType.IDENTIFIER, "y", Position(1, 13)),
            PrintScriptToken(TokenType.OPERATORS, "-", Position(1, 15)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "2", Position(1, 17)),
            PrintScriptToken(TokenType.DELIMITERS, ")", Position(1, 18)),
            PrintScriptToken(TokenType.DELIMITERS, ")", Position(1, 19)),
            PrintScriptToken(TokenType.DELIMITERS, ";", Position(1, 20))
        )

        val program = parseProgram(tokens)
        assertEquals(1, program.getStatements().size)
        val rootBinary = (program.getStatements()[0] as ExpressionStatement).getExpression() as BinaryExpression

        assertEquals("*", rootBinary.getOperator().getValue())
        val leftAddition = rootBinary.getLeft() as BinaryExpression
        val rightSubtraction = rootBinary.getRight() as BinaryExpression

        assertEquals("+", leftAddition.getOperator().getValue())
        assertEquals("-", rightSubtraction.getOperator().getValue())
        assertEquals("x", (leftAddition.getLeft() as IdentifierExpression).getName())
        assertEquals("1", (leftAddition.getRight() as LiteralExpression).getValue())
        assertEquals("y", (rightSubtraction.getLeft() as IdentifierExpression).getName())
        assertEquals("2", (rightSubtraction.getRight() as LiteralExpression).getValue())
    }
}