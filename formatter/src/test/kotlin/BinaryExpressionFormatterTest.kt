import builder.DefaultNodeBuilder
import coordinates.Position
import formatter.DefaultFormatter
import formatter.config.FormatConfig
import formatter.rules.RuleRegistry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import type.CommonTypes

class BinaryExpressionFormatterTest {
    private val builder = DefaultNodeBuilder()
    private val fmt = DefaultFormatter(RuleRegistry.rulesV10)
    private val pos = Position(1, 1)

    private fun tok(
        type: CommonTypes,
        value: String,
    ) = PrintScriptToken(type, value, pos)

    @Test
    fun `spaceAroundOperators true enforces single spaces`() {
        val lhs = builder.buildLiteralExpressionNode(tok(CommonTypes.NUMBER_LITERAL, "1"))
        val rhs = builder.buildLiteralExpressionNode(tok(CommonTypes.NUMBER_LITERAL, "2"))
        val expr = builder.buildBinaryExpressionNode(lhs, tok(CommonTypes.OPERATORS, "+"), rhs)
        val stmt = builder.buildExpressionStatementNode(expr)
        val config = FormatConfig(spaceAroundOperators = true)
        val result = fmt.formatToString(builder.buildProgramNode(listOf(stmt)), config)
        assertEquals("1 + 2;", result)
    }

    @Test
    fun `default operator spacing preserves raw token`() {
        val lhs = builder.buildLiteralExpressionNode(tok(CommonTypes.NUMBER_LITERAL, "3"))
        val opToken = tok(CommonTypes.OPERATORS, " * ")
        val rhs = builder.buildLiteralExpressionNode(tok(CommonTypes.NUMBER_LITERAL, "4"))
        val expr = builder.buildBinaryExpressionNode(lhs, opToken, rhs)
        val stmt = builder.buildExpressionStatementNode(expr)
        val result = fmt.formatToString(builder.buildProgramNode(listOf(stmt)), FormatConfig())
        assertEquals("3 * 4;", result)
    }
}
