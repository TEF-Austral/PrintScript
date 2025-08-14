import converter.factory.StringToTokenConverterFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reader.Reader
import reader.TxtReader
import splitter.SplitterFactory

class FullIntegrationTest {

    private lateinit var scriptReader: Reader
    private lateinit var largeScriptReader: Reader

    @BeforeEach
    fun setup() {
        val filePathScript = "src/test/resources/script.txt"
        scriptReader = TxtReader(filePathScript)
        val filePathLargerScript = "src/test/resources/largerScript.txt"
        largeScriptReader = TxtReader(filePathLargerScript)
    }

    @Test
    fun scriptTest() {
        val splitter = SplitterFactory.createSplitter()
        val tokenConverter = StringToTokenConverterFactory.createDefaultsTokenConverter()
        val lexer = PrintScriptLexer(scriptReader, splitter, tokenConverter)
        val tokens = lexer.tokenize()
        for (token in tokens) {
            println("Token: ${token.getValue()} Type: ${token.getType()} Coordinates: ${token.getCoordinates()}")
        }

        Assertions.assertEquals(5, tokens.size)

        Assertions.assertEquals(TokenType.DECLARATION, tokens[0].getType())
        Assertions.assertEquals("let", tokens[0].getValue())
        Assertions.assertEquals(1, tokens[0].getCoordinates().getRow())
        Assertions.assertEquals(1, tokens[0].getCoordinates().getColumn())

        Assertions.assertEquals(TokenType.IDENTIFIER, tokens[1].getType())
        Assertions.assertEquals("x", tokens[1].getValue())
        Assertions.assertEquals(1, tokens[1].getCoordinates().getRow())
        Assertions.assertEquals(5, tokens[1].getCoordinates().getColumn())

        Assertions.assertEquals(TokenType.ASSIGNMENT, tokens[2].getType())
        Assertions.assertEquals("=", tokens[2].getValue())
        Assertions.assertEquals(1, tokens[2].getCoordinates().getRow())
        Assertions.assertEquals(7, tokens[2].getCoordinates().getColumn())

        Assertions.assertEquals(TokenType.NUMBER_LITERAL, tokens[3].getType())
        Assertions.assertEquals("42", tokens[3].getValue())
        Assertions.assertEquals(1, tokens[3].getCoordinates().getRow())
        Assertions.assertEquals(9, tokens[3].getCoordinates().getColumn())

        Assertions.assertEquals(TokenType.DELIMITERS, tokens[4].getType())
        Assertions.assertEquals(";", tokens[4].getValue())
        Assertions.assertEquals(1, tokens[4].getCoordinates().getRow())
        Assertions.assertEquals(11, tokens[4].getCoordinates().getColumn())
    }

    @Test
    fun largerScriptTest() {
        val splitter = SplitterFactory.createSplitter()
        val tokenConverter = StringToTokenConverterFactory.createDefaultsTokenConverter()
        val lexer = PrintScriptLexer(largeScriptReader, splitter, tokenConverter)
        val tokens = lexer.tokenize()

        Assertions.assertEquals(70, tokens.size)

        Assertions.assertEquals(TokenType.DECLARATION, tokens[0].getType())
        Assertions.assertEquals("let", tokens[0].getValue())
        Assertions.assertEquals(TokenType.IDENTIFIER, tokens[1].getType())
        Assertions.assertEquals("a", tokens[1].getValue())
        Assertions.assertEquals(TokenType.ASSIGNMENT, tokens[2].getType())
        Assertions.assertEquals("=", tokens[2].getValue())
        Assertions.assertEquals(TokenType.NUMBER_LITERAL, tokens[3].getType())
        Assertions.assertEquals("20", tokens[3].getValue())
        Assertions.assertEquals(TokenType.DELIMITERS, tokens[4].getType())
        Assertions.assertEquals(";", tokens[4].getValue())

        Assertions.assertEquals(TokenType.OPERATORS, tokens[21].getType())
        Assertions.assertEquals("-", tokens[21].getValue())

        Assertions.assertEquals(TokenType.PRINT, tokens[38].getType())
        Assertions.assertEquals("println", tokens[38].getValue())

        Assertions.assertEquals(TokenType.RETURN, tokens[67].getType())
        Assertions.assertEquals("return", tokens[67].getValue())
        Assertions.assertEquals(TokenType.DELIMITERS, tokens[69].getType())
        Assertions.assertEquals(";", tokens[69].getValue())
    }
}
