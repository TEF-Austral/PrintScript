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
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.DECLARATION, "let", Position(1, 1)),
                PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
                PrintScriptToken(CommonTypes.ASSIGNMENT, "=", Position(1, 7)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 9)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 10)),
            )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertFalse(result.isSuccess())
        assertEquals("Semantic error: Expected delimiter Successfully executed: Method consume called advance", result.message())
    }

    @Test
    fun testInvalidVariableDeclarationMissingIdentifier() {
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.DECLARATION, "let", Position(1, 1)),
                PrintScriptToken(CommonTypes.DELIMITERS, ":", Position(1, 5)),
                PrintScriptToken(CommonTypes.NUMBER, "NUMBER", Position(1, 7)),
                PrintScriptToken(CommonTypes.ASSIGNMENT, "=", Position(1, 14)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 16)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 17)),
            )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertFalse(result.isSuccess())
        assertEquals("Semantic error: Expected identifier Successfully executed: Method consume called advance", result.message())
    }

    @Test
    fun testInvalidExpressionMissingOperand() {
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 1)),
                PrintScriptToken(CommonTypes.OPERATORS, "+", Position(1, 3)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 5)),
            )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertFalse(result.isSuccess())
        assertEquals("Surpassed parser length", result.message())
    }

    @Test
    fun testInvalidExpressionUnmatchedParentheses() {
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.DELIMITERS, "(", Position(1, 1)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 2)),
                PrintScriptToken(CommonTypes.OPERATORS, "+", Position(1, 4)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "3", Position(1, 6)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 7)),
            )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertFalse(result.isSuccess())
        assertEquals("Was expecting closing parenthesis", result.message())
    }

    @Test
    fun testInvalidVariableDeclarationMissingSemicolon() {
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.DECLARATION, "let", Position(1, 1)),
                PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
                PrintScriptToken(CommonTypes.DELIMITERS, ":", Position(1, 7)),
                PrintScriptToken(CommonTypes.NUMBER, "NUMBER", Position(1, 9)),
                PrintScriptToken(CommonTypes.ASSIGNMENT, "=", Position(1, 16)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 18)),
            )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertFalse(result.isSuccess())
        assertEquals("Semantic error: Variable declaration must end in ; ", result.message())
    }

    @Test
    fun testInvalidExpressionExtraClosingParenthesis() {
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 1)),
                PrintScriptToken(CommonTypes.OPERATORS, "+", Position(1, 3)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "3", Position(1, 5)),
                PrintScriptToken(CommonTypes.DELIMITERS, ")", Position(1, 6)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 7)),
            )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertFalse(result.isSuccess())
        assertEquals("Unexpected closing parenthesis", result.message())
    }

    @Test
    fun testInvalidExpressionConsecutiveOperators() {
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 1)),
                PrintScriptToken(CommonTypes.OPERATORS, "+", Position(1, 3)),
                PrintScriptToken(CommonTypes.OPERATORS, "+", Position(1, 5)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "3", Position(1, 7)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 8)),
            )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertFalse(result.isSuccess())
        assertEquals("Can't put operators side by side", result.message())
    }

    @Test
    fun testInvalidTokenSequenceEmptyParentheses() {
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.DELIMITERS, "(", Position(1, 1)),
                PrintScriptToken(CommonTypes.DELIMITERS, ")", Position(1, 2)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 3)),
            )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertFalse(result.isSuccess())
        assertEquals("Can't be handled currently by the statement parser", result.message())
    }

    @Test
    fun testInvalidVariableDeclarationWrongOrder() {
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.NUMBER, "NUMBER", Position(1, 1)),
                PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 8)),
                PrintScriptToken(CommonTypes.DELIMITERS, ":", Position(1, 10)),
                PrintScriptToken(CommonTypes.DECLARATION, "let", Position(1, 12)),
                PrintScriptToken(CommonTypes.ASSIGNMENT, "=", Position(1, 16)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 18)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 19)),
            )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertFalse(result.isSuccess())
        assertEquals( "Can't be handled currently by the statement parser", result.message())
    }

    @Test
    fun testUnexpectedTokenAfterValidStatement() {
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.DECLARATION, "let", Position(1, 1)),
                PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
                PrintScriptToken(CommonTypes.DELIMITERS, ":", Position(1, 7)),
                PrintScriptToken(CommonTypes.NUMBER, "NUMBER", Position(1, 9)),
                PrintScriptToken(CommonTypes.ASSIGNMENT, "=", Position(1, 16)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 18)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 19)),
                PrintScriptToken(CommonTypes.OPERATORS, "+", Position(2, 1)),
            )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertFalse(result.isSuccess())
        assertEquals("Can't be handled currently by the statement parser", result.message())
    }

    @Test
    fun testEmptyTokenList() {
        val tokens = emptyList<PrintScriptToken>()

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertTrue(result.isSuccess())
        assertEquals(0, result.getProgram().getStatements().size)
    }
}
