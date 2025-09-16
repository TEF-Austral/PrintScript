import converter.TokenConverter
import factory.StringSplitterFactory
import factory.StringToTokenConverterFactory
import formatter.DefaultFormatter
import formatter.config.FormatConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.StringReader

class NoSpacingSurroundingEqualsTest {

    private val tokenConverter: TokenConverter =
        StringToTokenConverterFactory
            .createDefaultsTokenConverter()

    private fun lex(input: String): TokenStream {
        val splitter = StringSplitterFactory.createStreamingSplitter(StringReader(input))
        var lexer: Lexer? = DefaultLexer(tokenConverter, splitter)

        val tokens = mutableListOf<Token>()
        while (lexer != null) {
            val result = lexer.next()
            if (result != null) {
                tokens.add(result.token)
                lexer = result.lexer
            } else {
                lexer = null
            }
        }
        return MockTokenStream(tokens)
    }

    @Test
    fun `test no spacing around equals, operators, and colon spacing`() {
        val config = FormatConfig(spaceAroundAssignment = false)
        val input = """
            let something: string= "a really cool thing";
            let another_thing: string ="another really cool thing";
            let twice_thing: string = "another really cool thing twice";
            let third_thing: string="another really cool thing three times";
        """.trimIndent()
        val expected = """
            let something: string="a really cool thing";
            let another_thing: string="another really cool thing";
            let twice_thing: string="another really cool thing twice";
            let third_thing: string="another really cool thing three times";
        """.trimIndent()

        val formatter = DefaultFormatter()
        val stream = lex(input)
        val output = formatter.formatToString(stream, config)

        assertEquals(expected, output)
    }
}
