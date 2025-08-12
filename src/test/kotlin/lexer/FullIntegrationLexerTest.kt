package lexer

import lexer.reader.Reader
import lexer.reader.TxtReader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import token.StringToTokenConverterFactory
import token.TokenType

class FullIntegrationLexerTest {

    private lateinit var reader: Reader

    @BeforeEach
    fun setup() {
        val filePath = "src/test/resources/script.txt"
        reader = TxtReader(filePath)
    }

    @Test
    fun fullIntegrationLexerTest() {
        val splitter = SplitterFactory.createSplitter()
        val tokenConverter = StringToTokenConverterFactory.createDefaultsTokenConverter()
        val lexer = PrintScriptLexer(reader, splitter, tokenConverter)
        val tokens = lexer.tokenize()

        assertEquals(5, tokens.size)

        assertEquals(TokenType.LET, tokens[0].getType())
        assertEquals("let", tokens[0].getValue())
        assertEquals(1, tokens[0].getCoordinates().getRow())
        assertEquals(1, tokens[0].getCoordinates().getColumn())

        assertEquals(TokenType.IDENTIFIER, tokens[1].getType())
        assertEquals("x", tokens[1].getValue())
        assertEquals(1, tokens[1].getCoordinates().getRow())
        assertEquals(5, tokens[1].getCoordinates().getColumn())

        assertEquals(TokenType.ASSIGN, tokens[2].getType())
        assertEquals("=", tokens[2].getValue())
        assertEquals(1, tokens[2].getCoordinates().getRow())
        assertEquals(7, tokens[2].getCoordinates().getColumn())

        assertEquals(TokenType.NUMBER_LITERAL, tokens[3].getType())
        assertEquals("42", tokens[3].getValue())
        assertEquals(1, tokens[3].getCoordinates().getRow())
        assertEquals(9, tokens[3].getCoordinates().getColumn())

        assertEquals(TokenType.SEMICOLON, tokens[4].getType())
        assertEquals(";", tokens[4].getValue())
        assertEquals(1, tokens[4].getCoordinates().getRow())
        assertEquals(11, tokens[4].getCoordinates().getColumn())
    }
}