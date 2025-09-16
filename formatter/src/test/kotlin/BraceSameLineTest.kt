import converter.TokenConverter
import factory.StringSplitterFactory
import factory.StringToTokenConverterFactory
import formatter.FormatterImpl
import formatter.config.FormatConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import stream.token.LexerTokenStream
import java.io.StringReader

class BraceSameLineTest {
    private val tokenConverter: TokenConverter =
        StringToTokenConverterFactory
            .createDefaultsTokenConverter()

    private fun lex(input: String): TokenStream {
        val splitter = StringSplitterFactory.createStreamingSplitter(StringReader(input))
        val lexer: Lexer = DefaultLexer(tokenConverter, splitter)
        val tokenStream = LexerTokenStream(lexer)
        return tokenStream
    }

    @Test
    fun `test if brace same line`() {
        val config = FormatConfig(
            ifBraceOnSameLine = true
        )

        val input = """
            let something: boolean = true;
            if (something)
            {
              println("Entered if");
            }
        """.trimIndent()

        val expected = """
            let something: boolean = true;
            if (something) {
              println("Entered if");
            }
        """.trimIndent()

        val stream = lex(input)
        val output = FormatterImpl().formatToString(stream, config)

        assertEquals(expected, output)
    }
}