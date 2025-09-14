import factory.StringToTokenConverterFactory
import converter.TokenConverter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import stringSplitter.Splitter
import factory.StringSplitterFactory
import type.CommonTypes

class LexerTest {
    private val splitter: Splitter = StringSplitterFactory.createDefaultsSplitter()
    private val tokenConverter: TokenConverter =
        StringToTokenConverterFactory
            .createDefaultsTokenConverter()

    private fun lex(input: String) =
        DefaultLexer(splitter, tokenConverter).tokenize(MockReader(input))

    @Test
    fun tokenizeDeclarationAssignmentWithNumberLiteral() {
        val tokens = lex("let x = 5;")
        assertEquals(5, tokens.size)

        assertEquals(CommonTypes.LET, tokens[0].getType())
        assertEquals("let", tokens[0].getValue())

        assertEquals(CommonTypes.IDENTIFIER, tokens[1].getType())
        assertEquals("x", tokens[1].getValue())

        assertEquals(CommonTypes.ASSIGNMENT, tokens[2].getType())
        assertEquals("=", tokens[2].getValue())

        assertEquals(CommonTypes.NUMBER_LITERAL, tokens[3].getType())
        assertEquals("5", tokens[3].getValue())

        assertEquals(CommonTypes.DELIMITERS, tokens[4].getType())
        assertEquals(";", tokens[4].getValue())
    }

    @Test
    fun tokenizeDeclarationConstAssignmentWithNumberLiteral() {
        val tokens = lex("const x = 5;")
        assertEquals(5, tokens.size)

        assertEquals(CommonTypes.CONST, tokens[0].getType())
        assertEquals("const", tokens[0].getValue())

        assertEquals(CommonTypes.IDENTIFIER, tokens[1].getType())
        assertEquals("x", tokens[1].getValue())

        assertEquals(CommonTypes.ASSIGNMENT, tokens[2].getType())
        assertEquals("=", tokens[2].getValue())

        assertEquals(CommonTypes.NUMBER_LITERAL, tokens[3].getType())
        assertEquals("5", tokens[3].getValue())

        assertEquals(CommonTypes.DELIMITERS, tokens[4].getType())
        assertEquals(";", tokens[4].getValue())
    }

    @Test
    fun tokenizeFunctionInvocationWithStringLiteral() {
        val tokens = lex("""println("hi")""")
        assertEquals(4, tokens.size)

        assertEquals(CommonTypes.PRINT, tokens[0].getType())
        assertEquals("println", tokens[0].getValue())

        assertEquals(CommonTypes.DELIMITERS, tokens[1].getType())
        assertEquals("(", tokens[1].getValue())

        assertEquals(CommonTypes.STRING_LITERAL, tokens[2].getType())
        assertEquals("hi", tokens[2].getValue())

        assertEquals(CommonTypes.DELIMITERS, tokens[3].getType())
        assertEquals(")", tokens[3].getValue())
    }

    @Test
    fun tokenizeArithmeticAndDelimiters() {
        val tokens = lex("(1+2)*3")
        assertEquals(7, tokens.size)

        assertEquals(CommonTypes.DELIMITERS, tokens[0].getType())
        assertEquals("(", tokens[0].getValue())

        assertEquals(CommonTypes.NUMBER_LITERAL, tokens[1].getType())
        assertEquals("1", tokens[1].getValue())

        assertEquals(CommonTypes.OPERATORS, tokens[2].getType())
        assertEquals("+", tokens[2].getValue())

        assertEquals(CommonTypes.NUMBER_LITERAL, tokens[3].getType())
        assertEquals("2", tokens[3].getValue())

        assertEquals(CommonTypes.DELIMITERS, tokens[4].getType())
        assertEquals(")", tokens[4].getValue())

        assertEquals(CommonTypes.OPERATORS, tokens[5].getType())
        assertEquals("*", tokens[5].getValue())

        assertEquals(CommonTypes.NUMBER_LITERAL, tokens[6].getType())
        assertEquals("3", tokens[6].getValue())
    }

    @Test
    fun tokenizeUnknownAsIdentifier() {
        val tokens = lex("foo")
        assertEquals(1, tokens.size)
        assertEquals(CommonTypes.IDENTIFIER, tokens[0].getType())
        assertEquals("foo", tokens[0].getValue())
    }

    @Test
    fun tokenizeConditionalsComparisonsAndLogicalOperators() {
        val tokens = lex("if (a<=10 && b!=0) return a")

        assertEquals(CommonTypes.CONDITIONALS, tokens[0].getType())
        assertEquals("if", tokens[0].getValue())

        assertEquals(CommonTypes.DELIMITERS, tokens[1].getType())
        assertEquals("(", tokens[1].getValue())

        assertEquals(CommonTypes.IDENTIFIER, tokens[2].getType())
        assertEquals("a", tokens[2].getValue())

        assertEquals(CommonTypes.COMPARISON, tokens[3].getType())
        assertEquals("<=", tokens[3].getValue())

        assertEquals(CommonTypes.NUMBER_LITERAL, tokens[4].getType())
        assertEquals("10", tokens[4].getValue())

        assertEquals(CommonTypes.LOGICAL_OPERATORS, tokens[5].getType())
        assertEquals("&&", tokens[5].getValue())

        assertEquals(CommonTypes.IDENTIFIER, tokens[6].getType())
        assertEquals("b", tokens[6].getValue())

        assertEquals(CommonTypes.COMPARISON, tokens[7].getType())
        assertEquals("!=", tokens[7].getValue())

        assertEquals(CommonTypes.NUMBER_LITERAL, tokens[8].getType())
        assertEquals("0", tokens[8].getValue())

        assertEquals(CommonTypes.DELIMITERS, tokens[9].getType())
        assertEquals(")", tokens[9].getValue())

        assertEquals(CommonTypes.IDENTIFIER, tokens[11].getType())
        assertEquals("a", tokens[11].getValue())
    }
}
