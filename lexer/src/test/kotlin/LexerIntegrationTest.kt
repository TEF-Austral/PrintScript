import converter.StringToTokenConverterFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reader.FileReader
import reader.Reader
import splitter.SplitterFactory

class LexerIntegrationTest {
    private lateinit var scriptReader: Reader
    private lateinit var largeScriptReader: Reader

    @BeforeEach
    fun setup() {
        val filePathScript = "src/test/resources/script.txt"
        scriptReader = FileReader(filePathScript)
        val filePathLargerScript = "src/test/resources/largerScript.txt"
        largeScriptReader = FileReader(filePathLargerScript)
    }

    @Test
    fun scriptTest() {
        val splitter = SplitterFactory.createSplitter()
        val tokenConverter = StringToTokenConverterFactory.createDefaultsTokenConverter()
        val lexer = Lexer(splitter, tokenConverter)
        val tokens = lexer.tokenize(scriptReader)
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
}
