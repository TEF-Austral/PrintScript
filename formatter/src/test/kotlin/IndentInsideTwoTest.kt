import converter.TokenConverter
import factory.StringSplitterFactory
import factory.StringToTokenConverterFactory
import formatter.DefaultFormatter
import formatter.config.FormatConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import stream.token.LexerTokenStream
import java.io.StringReader

class IndentInsideTwoTest {
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
    fun `test indent inside if with 4 spaces`() {
        val config =
            FormatConfig(
                indentSize = 4,
                ifBraceOnSameLine = true,
            )

        val input =
            """
            let something: boolean = true;
            if (something) {
              if (something) {
                println("Entered two ifs");
              }
            }
            """.trimIndent()

        val expected =
            """
            let something: boolean = true;
            if (something) {
                if (something) {
                    println("Entered two ifs");
                }
            }
            """.trimIndent()

        val stream = lex(input)
        val output = DefaultFormatter().formatToString(stream, config)

        assertEquals(expected, output)
    }
}
