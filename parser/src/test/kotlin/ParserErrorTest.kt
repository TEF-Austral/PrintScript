// parser/src/test/kotlin/ParserErrorTest.kt
import builder.DefaultNodeBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.factory.RecursiveParserFactory
import parser.result.ParseResult

class ParserErrorTest {
    @Test
    fun testMissingTypeInDeclarationReturnsFailure() {
        val tokens =
            listOf(
                PrintScriptToken(TokenType.DECLARATION, "let", Position(1, 1)),
                PrintScriptToken(TokenType.IDENTIFIER, "x", Position(1, 5)),
                PrintScriptToken(TokenType.DELIMITERS, ":", Position(1, 6)),
                PrintScriptToken(TokenType.DELIMITERS, ";", Position(1, 7)),
            )

        val parser = RecursiveParserFactory().createParser(tokens, DefaultNodeBuilder())
        val result = parser.parse()
        assertTrue(result is ParseResult.Failure)
        val error = (result as ParseResult.Failure).error

        assertEquals("Expected DATA_TYPES but found DELIMITERS", error.message)
        assertEquals(1, error.position.getRow())
        assertEquals(7, error.position.getColumn())
    }

    @Test
    fun testMissingSemicolonInDeclarationReturnsFailure() {
        val tokens =
            listOf(
                PrintScriptToken(TokenType.DECLARATION, "let", Position(1, 1)),
                PrintScriptToken(TokenType.IDENTIFIER, "x", Position(1, 5)),
                PrintScriptToken(TokenType.DELIMITERS, ":", Position(1, 6)),
                PrintScriptToken(TokenType.DATA_TYPES, "NUMBER", Position(1, 8)),
            )

        val parser = RecursiveParserFactory().createParser(tokens, DefaultNodeBuilder())
        val result = parser.parse()
        assertTrue(result is ParseResult.Failure)
        val error = (result as ParseResult.Failure).error

        assertEquals("Expected DELIMITERS but found end-of-input", error.message)
        assertEquals(-1, error.position.getRow())
        assertEquals(-1, error.position.getColumn())
    }

    @Test
    fun testAssignmentMissingTerminatorReturnsFailure() {
        val tokens =
            listOf(
                PrintScriptToken(TokenType.IDENTIFIER, "x", Position(1, 1)),
                PrintScriptToken(TokenType.ASSIGNMENT, "=", Position(1, 3)),
                PrintScriptToken(TokenType.DELIMITERS, ";", Position(1, 4)),
            )

        val parser = RecursiveParserFactory().createParser(tokens, DefaultNodeBuilder())
        val result = parser.parse()
        assertTrue(result is ParseResult.Failure)
        val error = (result as ParseResult.Failure).error

        assertEquals("Expected DELIMITERS but found end-of-input", error.message)
        assertEquals(-1, error.position.getRow())
        assertEquals(-1, error.position.getColumn())
    }
}
