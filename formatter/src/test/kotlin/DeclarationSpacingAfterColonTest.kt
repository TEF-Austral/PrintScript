import converter.TokenConverter
import factory.StringSplitterFactory
import factory.StringToTokenConverterFactory
import formatter.DefaultFormatter
import formatter.config.FormatConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import stream.token.LexerTokenStream
import java.io.StringReader

class DeclarationSpacingAfterColonTest {
    private val tokenConverter: TokenConverter =
        StringToTokenConverterFactory
            .createDefaultsTokenConverter()

    private fun lex(input: String): TokenStream {
        val splitter = StringSplitterFactory.createStreamingSplitter(StringReader(input))
        val lexer: Lexer = DefaultLexer(tokenConverter, splitter)
        val tokenStream = LexerTokenStream(lexer)
        return tokenStream
    }

    private fun lex2(input: String): List<Token> {
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
    fun `test enforce spacing after colon in declaration`() {
        val config =
            FormatConfig(
                spaceAfterColon = true,
            )

        val input =
            """
            let something:string = "a really cool thing";
            let another_thing: string = "another really cool thing";
            let twice_thing : string = "another really cool thing twice";
            let third_thing :string="another really cool thing three times";
            """.trimIndent()

        val expected =
            """
            let something: string = "a really cool thing";
            let another_thing: string = "another really cool thing";
            let twice_thing : string = "another really cool thing twice";
            let third_thing : string="another really cool thing three times";
            """.trimIndent()

        val stream2 = lex2(input)
        val stream = MockTokenStream(stream2)
        val output = DefaultFormatter().formatToString(stream, config)

        assertEquals(expected, output)
    }
}
