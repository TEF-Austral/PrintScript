import builder.DefaultNodeBuilder
import coordinates.Position
import parser.factory.RecursiveParserFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import type.CommonTypes

class ParserErrorTest {

    @Test
    fun testInvalidVariableDeclarationMissingType() {
        // Invalid: "let x = 5;" (missing type declaration)
        val tokens = listOf(
            PrintScriptToken(CommonTypes.DECLARATION, "let", Position(1, 1)),
            PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
            PrintScriptToken(CommonTypes.ASSIGNMENT, "=", Position(1, 7)),
            PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 9)),
            PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 10))
        )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        // Should fail due to missing colon and type
        assertFalse(result.isSuccess())
        assertTrue(result.message().contains("Error"))
    }

    @Test
    fun testInvalidVariableDeclarationMissingIdentifier() {
        // Invalid: "let : NUMBER = 5;" (missing identifier)
        val tokens = listOf(
            PrintScriptToken(CommonTypes.DECLARATION, "let", Position(1, 1)),
            PrintScriptToken(CommonTypes.DELIMITERS, ":", Position(1, 5)),
            PrintScriptToken(CommonTypes.NUMBER, "NUMBER", Position(1, 7)),
            PrintScriptToken(CommonTypes.ASSIGNMENT, "=", Position(1, 14)),
            PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 16)),
            PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 17))
        )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertFalse(result.isSuccess())
        assertTrue(result.message().contains("Error"))
    }

    @Test
    fun testInvalidExpressionMissingOperand() {
        // Invalid: "5 + ;" (missing right operand)
        val tokens = listOf(
            PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 1)),
            PrintScriptToken(CommonTypes.OPERATORS, "+", Position(1, 3)),
            PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 5))
        )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertFalse(result.isSuccess())
        assertTrue(result.message().contains("Error"))
    }

    @Test
    fun testInvalidExpressionUnmatchedParentheses() {
        // Invalid: "(5 + 3;" (missing closing parenthesis)
        val tokens = listOf(
            PrintScriptToken(CommonTypes.DELIMITERS, "(", Position(1, 1)),
            PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 2)),
            PrintScriptToken(CommonTypes.OPERATORS, "+", Position(1, 4)),
            PrintScriptToken(CommonTypes.NUMBER_LITERAL, "3", Position(1, 6)),
            PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 7))
        )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertFalse(result.isSuccess())
        assertTrue(result.message().contains("Error"))
    }

    @Test
    fun testInvalidExpressionExtraClosingParenthesis() {
        // Invalid: "5 + 3);" (extra closing parenthesis)
        val tokens = listOf(
            PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 1)),
            PrintScriptToken(CommonTypes.OPERATORS, "+", Position(1, 3)),
            PrintScriptToken(CommonTypes.NUMBER_LITERAL, "3", Position(1, 5)),
            PrintScriptToken(CommonTypes.DELIMITERS, ")", Position(1, 6)),
            PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 7))
        )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertFalse(result.isSuccess())
        assertTrue(result.message().contains("Error"))
    }

    @Test
    fun testInvalidVariableDeclarationMissingSemicolon() {
        // Invalid: "let x: NUMBER = 5" (missing semicolon)
        val tokens = listOf(
            PrintScriptToken(CommonTypes.DECLARATION, "let", Position(1, 1)),
            PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
            PrintScriptToken(CommonTypes.DELIMITERS, ":", Position(1, 7)),
            PrintScriptToken(CommonTypes.NUMBER, "NUMBER", Position(1, 9)),
            PrintScriptToken(CommonTypes.ASSIGNMENT, "=", Position(1, 16)),
            PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 18))
        )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertFalse(result.isSuccess())
        assertTrue(result.message().contains("Error"))
    }

    @Test
    fun testInvalidExpressionConsecutiveOperators() {
        // Invalid: "5 + + 3;" (consecutive operators)
        val tokens = listOf(
            PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 1)),
            PrintScriptToken(CommonTypes.OPERATORS, "+", Position(1, 3)),
            PrintScriptToken(CommonTypes.OPERATORS, "+", Position(1, 5)),
            PrintScriptToken(CommonTypes.NUMBER_LITERAL, "3", Position(1, 7)),
            PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 8))
        )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertFalse(result.isSuccess())
        assertTrue(result.message().contains("Error"))
    }

    @Test
    fun testInvalidTokenSequenceEmptyParentheses() {
        // Invalid: "();" (empty parentheses)
        val tokens = listOf(
            PrintScriptToken(CommonTypes.DELIMITERS, "(", Position(1, 1)),
            PrintScriptToken(CommonTypes.DELIMITERS, ")", Position(1, 2)),
            PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 3))
        )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertFalse(result.isSuccess())
        assertTrue(result.message().contains("Error"))
    }

    @Test
    fun testInvalidVariableDeclarationWrongOrder() {
        // Invalid: "NUMBER x: let = 5;" (wrong order of tokens)
        val tokens = listOf(
            PrintScriptToken(CommonTypes.NUMBER, "NUMBER", Position(1, 1)),
            PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 8)),
            PrintScriptToken(CommonTypes.DELIMITERS, ":", Position(1, 10)),
            PrintScriptToken(CommonTypes.DECLARATION, "let", Position(1, 12)),
            PrintScriptToken(CommonTypes.ASSIGNMENT, "=", Position(1, 16)),
            PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 18)),
            PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 19))
        )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertFalse(result.isSuccess())
        assertTrue(result.message().contains("Error"))
    }

    @Test
    fun testInvalidExpressionMissingOperator() {
        // Invalid: "5 3;" (missing operator between operands)
        val tokens = listOf(
            PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 1)),
            PrintScriptToken(CommonTypes.NUMBER_LITERAL, "3", Position(1, 3)),
            PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 4))
        )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertFalse(result.isSuccess())
        assertTrue(result.message().contains("Error"))
    }

    @Test
    fun testEmptyTokenList() {
        // Test with completely empty token list
        val tokens = emptyList<PrintScriptToken>()

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        // Empty program should be valid and return success with empty statements
        assertTrue(result.isSuccess())
        assertEquals(0, result.getProgram().getStatements().size)
    }

    @Test
    fun testUnexpectedTokenAfterValidStatement() {
        // Valid statement followed by unexpected token: "let x: NUMBER = 5; UNEXPECTED"
        val tokens = listOf(
            PrintScriptToken(CommonTypes.DECLARATION, "let", Position(1, 1)),
            PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
            PrintScriptToken(CommonTypes.DELIMITERS, ":", Position(1, 7)),
            PrintScriptToken(CommonTypes.NUMBER, "NUMBER", Position(1, 9)),
            PrintScriptToken(CommonTypes.ASSIGNMENT, "=", Position(1, 16)),
            PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 18)),
            PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 19)),
            PrintScriptToken(CommonTypes.OPERATORS, "+", Position(2, 1)) // Unexpected operator without context
        )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertFalse(result.isSuccess())
        assertTrue(result.message().contains("Error"))
    }
}