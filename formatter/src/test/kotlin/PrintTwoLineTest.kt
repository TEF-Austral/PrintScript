import converter.TokenConverter
import factory.StringSplitterFactory
import factory.StringToTokenConverterFactory
import formatter.DefaultFormatter
import formatter.config.FormatConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import stream.token.LexerTokenStream
import java.io.StringReader

class PrintTwoLineTest {
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
    fun `test two line breaks after println`() {
        val config =
            FormatConfig(
                blankLinesAfterPrintln = 2,
            )

        val input =
            """
            let something:string = "a really cool thing";
            println(something);
            println("in the way she moves");
            """.trimIndent()

        val expected =
            """
            let something:string = "a really cool thing";
            println(something);
            
            
            println("in the way she moves");
            """.trimIndent()

        val stream = lex(input)
        val output = DefaultFormatter().formatToString(stream, config)

        assertEquals(expected, output)
    }
}
