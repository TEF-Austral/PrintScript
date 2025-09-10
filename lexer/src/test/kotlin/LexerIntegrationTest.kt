import converter.StringToTokenConverterFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import stringSplitter.SplitterFactory
import type.CommonTypes

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
        val lexer = DefaultLexer(splitter, tokenConverter)
        val tokens = lexer.tokenize(scriptReader)
        for (token in tokens) {
            println("Token: ${token.getValue()} Type: ${token.getType()} Coordinates: ${token.getCoordinates()}")
        }

        Assertions.assertEquals(5, tokens.size)

        Assertions.assertEquals(CommonTypes.LET, tokens[0].getType())
        Assertions.assertEquals("let", tokens[0].getValue())
        Assertions.assertEquals(1, tokens[0].getCoordinates().getRow())
        Assertions.assertEquals(1, tokens[0].getCoordinates().getColumn())

        Assertions.assertEquals(CommonTypes.IDENTIFIER, tokens[1].getType())
        Assertions.assertEquals("x", tokens[1].getValue())
        Assertions.assertEquals(1, tokens[1].getCoordinates().getRow())
        Assertions.assertEquals(5, tokens[1].getCoordinates().getColumn())

        Assertions.assertEquals(CommonTypes.ASSIGNMENT, tokens[2].getType())
        Assertions.assertEquals("=", tokens[2].getValue())
        Assertions.assertEquals(1, tokens[2].getCoordinates().getRow())
        Assertions.assertEquals(7, tokens[2].getCoordinates().getColumn())

        Assertions.assertEquals(CommonTypes.NUMBER_LITERAL, tokens[3].getType())
        Assertions.assertEquals("42", tokens[3].getValue())
        Assertions.assertEquals(1, tokens[3].getCoordinates().getRow())
        Assertions.assertEquals(9, tokens[3].getCoordinates().getColumn())

        Assertions.assertEquals(CommonTypes.DELIMITERS, tokens[4].getType())
        Assertions.assertEquals(";", tokens[4].getValue())
        Assertions.assertEquals(1, tokens[4].getCoordinates().getRow())
        Assertions.assertEquals(11, tokens[4].getCoordinates().getColumn())
    }

    @Test
    fun largerScriptTest() {
        val content = largeScriptReader.read()
        println("Contenido leído: '$content'") // Debug: ver qué lee
        println("Longitud: ${content.length}")

        val splitter = SplitterFactory.createSplitter()
        val tokenConverter = StringToTokenConverterFactory.createDefaultsTokenConverter()
        val lexer = DefaultLexer(splitter, tokenConverter)
        val tokens = lexer.tokenize(largeScriptReader)

        println("Total tokens: ${tokens.size}") // Debug: cuántos tokens
        for (token in tokens) {
            println("Token: ${token.getValue()} Type: ${token.getType()} Coordinates: ${token.getCoordinates()}")
        }
    }

    @Test
    fun simpleDeclarationTest() {
        val splitter = SplitterFactory.createSplitter()
        val tokenConverter = StringToTokenConverterFactory.createDefaultsTokenConverter()
        val lexer = DefaultLexer(splitter, tokenConverter)
        val filePathScriptV2 = "src/test/resources/scriptV2.txt"
        val scriptReader = FileReader(filePathScriptV2)
        val tokens = lexer.tokenize(scriptReader)

        for (token in tokens) {
            println("Token: ${token.getValue()} Type: ${token.getType()} Coordinates: ${token.getCoordinates()}")
        }

        // let x:String = "123";
        // let y:Number = 123;

        Assertions.assertEquals(14, tokens.size)
        Assertions.assertEquals(CommonTypes.LET, tokens[0].getType())
        Assertions.assertEquals("let", tokens[0].getValue())
        Assertions.assertEquals(1, tokens[0].getCoordinates().getRow())
        Assertions.assertEquals(1, tokens[0].getCoordinates().getColumn())
        Assertions.assertEquals(CommonTypes.IDENTIFIER, tokens[1].getType())
    }
}
