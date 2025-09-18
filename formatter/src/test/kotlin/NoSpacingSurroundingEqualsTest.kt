import converter.TokenConverter
import factory.StringSplitterFactory
import factory.StringToTokenConverterFactory
import formatter.DefaultFormatter
import formatter.config.FormatConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import stream.token.LexerTokenStream
import java.io.StringReader

class NoSpacingSurroundingEqualsTest {
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
    fun `test no spacing around equals, operators, and colon spacing`() {
        val config =
            FormatConfig(
                spaceAroundAssignment = false,
                spaceBeforeColon = false, // Agregar configuración para colon
                spaceAfterColon = true, // Mantener espacio después de colon
            )

        val input =
            """
            let something: string= "a really cool thing";
            let another_thing: string ="another really cool thing";
            let twice_thing: string = "another really cool thing twice";
            let third_thing: string="another really cool thing three times";
            """.trimIndent()

        val expected =
            """
            let something: string="a really cool thing";
            let another_thing: string="another really cool thing";
            let twice_thing: string="another really cool thing twice";
            let third_thing: string="another really cool thing three times";
            """.trimIndent()

        val stream = lex(input)
        val output = DefaultFormatter().formatToString(stream, config)

        assertEquals(expected, output)
    }
}
