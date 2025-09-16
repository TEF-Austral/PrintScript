import converter.TokenConverter
import factory.StringSplitterFactory
import factory.StringToTokenConverterFactory
import formatter.DefaultFormatter
import formatter.config.FormatConfig
import formatter.factory.FormatterFactory
import formatter.rules.*
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
        val config = FormatConfig(
            spaceAroundAssignment = false,
            spaceBeforeColon = false,  // Agregar configuración para colon
            spaceAfterColon = true      // Mantener espacio después de colon
        )

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

        // IMPORTANTE: Crear el formatter con las reglas usando FormatterFactory
        val formatter = FormatterFactory().createFormatter()
        // O alternativamente, crear con las reglas directamente:
        // val formatter = createFormatterWithRules()

        val stream = lex(input)
        val output = formatter.formatToString(stream, config)

        assertEquals(expected, output)
    }

    // Método helper para crear formatter con reglas
    private fun createFormatterWithRules(): DefaultFormatter {
        val rules = listOf(
            LineBreakAfterSemicolonRule(),
            NormalizeSpacesInTokenRule(),
            LineBreaksAfterPrintlnRule(),
            ColonSpacingRule(),
            SpaceAroundAssignmentRule(),
            SpaceAroundOperatorsRule(),
            IfBraceRule()
        )
        return DefaultFormatter(rules)
    }
}
