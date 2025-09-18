import converter.TokenConverter
import factory.StringSplitterFactory
import factory.StringToTokenConverterFactory
import formatter.DefaultFormatter
import formatter.config.FormatConfig
import java.io.StringReader
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import stream.token.LexerTokenStream

class ColonRuleTest {

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
    fun `colon remains unchanged when colon spacing not configured`() {
        val config = FormatConfig()

        val input =
            """
            let value:number;
            """.trimIndent()

        val expected =
            """
            let value:number;
            """.trimIndent()

        val stream = lex(input)
        val output = DefaultFormatter().formatToString(stream, config)

        assertEquals(expected, output)
    }

    @Test
    fun `adds space after colon when only spaceAfterColon true`() {
        val config =
            FormatConfig(
                spaceAfterColon = true,
            )

        val input =
            """
            let value:number;
            """.trimIndent()

        val expected =
            """
            let value: number;
            """.trimIndent()

        val stream = lex(input)
        val output = DefaultFormatter().formatToString(stream, config)

        assertEquals(expected, output)
    }

    @Test
    fun `adds space before colon when only spaceBeforeColon true`() {
        val config =
            FormatConfig(
                spaceBeforeColon = true,
            )

        val input =
            """
            let value:number;
            """.trimIndent()

        val expected =
            """
            let value :number;
            """.trimIndent()

        val stream = lex(input)
        val output = DefaultFormatter().formatToString(stream, config)

        assertEquals(expected, output)
    }

    @Test
    fun `adds spaces both before and after colon when both configured true`() {
        val config =
            FormatConfig(
                spaceBeforeColon = true,
                spaceAfterColon = true,
            )

        val input =
            """
            let value:number;
            """.trimIndent()

        val expected =
            """
            let value : number;
            """.trimIndent()

        val stream = lex(input)
        val output = DefaultFormatter().formatToString(stream, config)

        assertEquals(expected, output)
    }
}
