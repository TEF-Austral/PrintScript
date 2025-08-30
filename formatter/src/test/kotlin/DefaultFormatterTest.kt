import builder.DefaultNodeBuilder
import formatter.DefaultFormatter
import formatter.config.FormatConfig
import node.LiteralExpression
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.StringWriter

class DefaultFormatterTest {
    private val builder = DefaultNodeBuilder()
    private val fmt = DefaultFormatter()

    private fun tok(
        type: TokenType,
        value: String,
    ) = PrintScriptToken(type, value, Position(1, 1))

    @Test
    fun `declaration with default config`() {
        val decl =
            builder.buildVariableDeclarationStatementNode(
                identifier = tok(TokenType.IDENTIFIER, "x"),
                dataType = tok(TokenType.DATA_TYPES, "number"),
                initialValue = null,
            )

        val program = builder.buildProgramNode(listOf(decl))
        val result = fmt.formatToString(program, FormatConfig())

        assertEquals("let x: number;\n\n", result)
    }

    @Test
    fun `assignment with no spaces around equals`() {
        val assign =
            builder.buildAssignmentStatementNode(
                identifier = tok(TokenType.IDENTIFIER, "a"),
                value = LiteralExpression(tok(TokenType.NUMBER_LITERAL, "42")),
            )

        val config = FormatConfig(spaceAroundAssignment = false)
        val program = builder.buildProgramNode(listOf(assign))
        val result = fmt.formatToString(program, config)

        assertEquals("a=42;\n\n", result)
    }

    @Test
    fun `println with blank line before`() {
        val printStmt =
            builder.buildPrintStatementNode(
                builder.buildLiteralExpressionNode(tok(TokenType.STRING_LITERAL, "\"hi\"")),
            )

        val config = FormatConfig(blankLinesBeforePrintln = 1)
        val program = builder.buildProgramNode(listOf(printStmt))
        val result = fmt.formatToString(program, config)

        assertEquals("\nprintln(\"hi\");\n\n", result)
    }

    @Test
    fun `custom colon spacing in declaration`() {
        val decl =
            builder.buildVariableDeclarationStatementNode(
                identifier = tok(TokenType.IDENTIFIER, "msg"),
                dataType = tok(TokenType.DATA_TYPES, "string"),
                initialValue = LiteralExpression(tok(TokenType.STRING_LITERAL, "\"ok\"")),
            )

        val config =
            FormatConfig(
                spaceBeforeColon = true,
                spaceAfterColon = false,
                spaceAroundAssignment = true,
            )
        val program = builder.buildProgramNode(listOf(decl))
        val result = fmt.formatToString(program, config)

        assertEquals("let msg :string = \"ok\";\n\n", result)
    }

    @Test
    fun `formatToWriter writes declaration with default config`() {
        val decl =
            builder.buildVariableDeclarationStatementNode(
                identifier = tok(TokenType.IDENTIFIER, "x"),
                dataType = tok(TokenType.DATA_TYPES, "number"),
                initialValue = null,
            )
        val program = builder.buildProgramNode(listOf(decl))
        val writer = StringWriter()

        fmt.formatToWriter(program, FormatConfig(), writer)

        assertEquals("let x: number;\n\n", writer.toString())
    }

    @Test
    fun `formatToWriter writes assignment without spaces around equals`() {
        val assign =
            builder.buildAssignmentStatementNode(
                identifier = tok(TokenType.IDENTIFIER, "a"),
                value = LiteralExpression(tok(TokenType.NUMBER_LITERAL, "42")),
            )
        val config = FormatConfig(spaceAroundAssignment = false)
        val program = builder.buildProgramNode(listOf(assign))
        val writer = StringWriter()

        fmt.formatToWriter(program, config, writer)

        assertEquals("a=42;\n\n", writer.toString())
    }
}
