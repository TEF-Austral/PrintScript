package lexer

import lexer.reader.MockReader
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import token.StringToTokenConverterFactory
import token.TokenConverter
import token.TokenType

class LexerTest {

    private val splitter: Splitter = SplitterFactory().createSplitter()
    private val tokenConverter: TokenConverter = StringToTokenConverterFactory.createDefaultsTokenConverter()

    private fun lex(input: String) =
        PrintScriptLexer(MockReader(input), splitter, tokenConverter).tokenize()

    @Test
    fun tokenizeLetAssignmentWithNumberLiteral() {
        val tokens = lex("let x = 5;")
        assertEquals(5, tokens.size)
        assertEquals(TokenType.LET, tokens[0].getType());      assertEquals("let", tokens[0].getValue())
        assertEquals(TokenType.IDENTIFIER, tokens[1].getType());assertEquals("x", tokens[1].getValue())
        assertEquals(TokenType.ASSIGN, tokens[2].getType());    assertEquals("=", tokens[2].getValue())
        assertEquals(TokenType.NUMBER_LITERAL, tokens[3].getType()); assertEquals("5", tokens[3].getValue())
        assertEquals(TokenType.SEMICOLON, tokens[4].getType()); assertEquals(";", tokens[4].getValue())
    }

    @Test
    fun tokenizePrintWithStringLiteral() {
        val tokens = lex("""println("hi")""")
        assertEquals(4, tokens.size)
        assertEquals(TokenType.PRINT, tokens[0].getType());             assertEquals("println", tokens[0].getValue())
        assertEquals(TokenType.OPEN_PARENTHESIS, tokens[1].getType());  assertEquals("(", tokens[1].getValue())
        assertEquals(TokenType.STRING_LITERAL, tokens[2].getType());    assertEquals("\"hi\"", tokens[2].getValue())
        assertEquals(TokenType.CLOSE_PARENTHESIS, tokens[3].getType()); assertEquals(")", tokens[3].getValue())
    }

    @Test
    fun tokenizeArithmeticAndParens() {
        val tokens = lex("(1+2)*3")
        assertEquals(7, tokens.size)
        assertEquals(TokenType.OPEN_PARENTHESIS, tokens[0].getType());  assertEquals("(", tokens[0].getValue())
        assertEquals(TokenType.NUMBER_LITERAL, tokens[1].getType());    assertEquals("1", tokens[1].getValue())
        assertEquals(TokenType.PLUS, tokens[2].getType());              assertEquals("+", tokens[2].getValue())
        assertEquals(TokenType.NUMBER_LITERAL, tokens[3].getType());    assertEquals("2", tokens[3].getValue())
        assertEquals(TokenType.CLOSE_PARENTHESIS, tokens[4].getType()); assertEquals(")", tokens[4].getValue())
        assertEquals(TokenType.MULTIPLY, tokens[5].getType());          assertEquals("*", tokens[5].getValue())
        assertEquals(TokenType.NUMBER_LITERAL, tokens[6].getType());    assertEquals("3", tokens[6].getValue())
    }

    @Test
    fun tokenizeUnknownAsIdentifier() {
        val tokens = lex("foo")
        assertEquals(1, tokens.size)
        assertEquals(TokenType.IDENTIFIER, tokens[0].getType())
        assertEquals("foo", tokens[0].getValue())
    }

    @Test
    fun tokenizeRelationalAndLogical() {
        val tokens = lex("if (a<=10 && b!=0) return a")
        assertEquals(TokenType.IF, tokens[0].getType());                         assertEquals("if", tokens[0].getValue())
        assertEquals(TokenType.OPEN_PARENTHESIS, tokens[1].getType());           assertEquals("(", tokens[1].getValue())
        assertEquals(TokenType.IDENTIFIER, tokens[2].getType());                 assertEquals("a", tokens[2].getValue())
        assertEquals(TokenType.LESS_THAN_OR_EQUAL, tokens[3].getType());         assertEquals("<=", tokens[3].getValue())
        assertEquals(TokenType.NUMBER_LITERAL, tokens[4].getType());             assertEquals("10", tokens[4].getValue())
        assertEquals(TokenType.AND, tokens[5].getType());                        assertEquals("&&", tokens[5].getValue())
        assertEquals(TokenType.IDENTIFIER, tokens[6].getType());                 assertEquals("b", tokens[6].getValue())
        assertEquals(TokenType.NOT_EQUALS, tokens[7].getType());                 assertEquals("!=", tokens[7].getValue())
        assertEquals(TokenType.NUMBER_LITERAL, tokens[8].getType());             assertEquals("0", tokens[8].getValue())
        assertEquals(TokenType.CLOSE_PARENTHESIS, tokens[9].getType());          assertEquals(")", tokens[9].getValue())
        assertEquals(TokenType.RETURN, tokens[10].getType());                    assertEquals("return", tokens[10].getValue())
        assertEquals(TokenType.IDENTIFIER, tokens[11].getType());                assertEquals("a", tokens[11].getValue())
    }
}
