import builder.DefaultNodeBuilder
import coordinates.Position
import formatter.DefaultFormatter
import formatter.config.FormatConfig
import formatter.rules.RuleRegistry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import type.CommonTypes

class EnforceSingleSpaceFormatterTest {
    private val builder = DefaultNodeBuilder()
    private val fmt = DefaultFormatter(RuleRegistry.rulesV10)
    private val pos = Position(1, 1)

    private fun tok(
        type: CommonTypes,
        value: String,
    ) = PrintScriptToken(type, value, pos)

    @Test
    fun `enforceSingleSpace collapses multiple spaces in raw tokens`() {
        // supply dataType token with two leading spaces
        val decl =
            builder.buildVariableDeclarationStatementNode(
                tok(CommonTypes.LET, "let"),
                tok(CommonTypes.IDENTIFIER, "x"),
                tok(CommonTypes.NUMBER, "  number"),
                initialValue = null,
            )
        val config = FormatConfig(enforceSingleSpace = true)
        val result = fmt.formatToString(builder.buildProgramNode(listOf(decl)), config)
        // extra spaces after colon should collapse to one
        assertEquals("let x: number;", result)
    }
}
