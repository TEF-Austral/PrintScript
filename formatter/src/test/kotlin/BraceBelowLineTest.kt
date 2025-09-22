import converter.TokenConverter
import factory.StringSplitterFactory
import factory.StringToTokenConverterFactory
import formatter.DefaultFormatter
import formatter.config.FormatConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import stream.token.LexerTokenStream
import java.io.StringReader

class BraceBelowLineTest {
    private val tokenConverter: TokenConverter =
        StringToTokenConverterFactory
            .createDefaultsTokenConverter()

    private fun lex(input: String): TokenStream {
        val splitter = StringSplitterFactory.createStreamingSplitter(StringReader(input))
        val lexer: Lexer = DefaultLexer(tokenConverter, splitter)
        val tokenStream = LexerTokenStream(lexer)
        return tokenStream
    }

    private fun completelex(input: String): List<Token> {
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
        return tokens
    }

    @Test
    fun `test if brace below line`() {
        val config =
            FormatConfig(
                ifBraceOnSameLine = false, // "if-brace-below-line": true
            )

        val input =
            """
            let something: boolean = true;
            if (something) {
              println("Entered if");
            }
            """

        val expected =
            """
            let something: boolean = true;
            if (something)
            {
              println("Entered if");
            }
            """.trimIndent()

        val stream = lex(input)
        val output = DefaultFormatter().formatToString(stream, config)

        assertEquals(expected, output)
    }
}
