import builder.DefaultNodeBuilder
import coordinates.Position
import formatter.DefaultFormatter
import formatter.config.FormatConfig
import formatter.rules.RuleRegistry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import type.CommonTypes

class BreakAfterStatementFormatterTest {
    private val builder = DefaultNodeBuilder()
    private val fmt = DefaultFormatter(RuleRegistry.rulesV10)
    private val pos = Position(1, 1)

    private fun tok(
        type: CommonTypes,
        value: String,
    ) = PrintScriptToken(type, value, pos)

    @Test
    fun `breakAfterStatement false removes trailing newline for print`() {
        val stmt =
            builder.buildPrintStatementNode(
                builder.buildLiteralExpressionNode(tok(CommonTypes.STRING_LITERAL, "hi")),
            )
        val config = FormatConfig(breakAfterStatement = false)
        val result = fmt.formatToString(builder.buildProgramNode(listOf(stmt)), config)
        assertEquals("println(\"hi\");", result)
    }

    @Test
    fun `breakAfterStatement false removes trailing newline for assignment`() {
        val stmt =
            builder.buildAssignmentStatementNode(
                identifier = tok(CommonTypes.IDENTIFIER, "a"),
                value = builder.buildLiteralExpressionNode(tok(CommonTypes.NUMBER_LITERAL, "5")),
            )
        val config = FormatConfig(breakAfterStatement = false)
        val result = fmt.formatToString(builder.buildProgramNode(listOf(stmt)), config)
        assertEquals("a=5;", result)
    }
}
