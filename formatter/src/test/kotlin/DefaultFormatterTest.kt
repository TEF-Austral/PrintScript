import builder.DefaultNodeBuilder
import coordinates.Position
import formatter.DefaultFormatter
import formatter.config.FormatConfig
import node.LiteralExpression
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.StringWriter
import type.CommonTypes

class DefaultFormatterTest {
    private val builder = DefaultNodeBuilder()
    private val fmt = DefaultFormatter()

    private fun tok(
        type: CommonTypes,
        value: String,
    ) = PrintScriptToken(type, value, Position(1, 1))

    @Test
    fun `declaration with default config`() {
        val decl =
            builder.buildVariableDeclarationStatementNode(
                tok(CommonTypes.LET, "let"),
                identifier = tok(CommonTypes.IDENTIFIER, "x"),
                dataType = tok(CommonTypes.NUMBER, "number"),
                initialValue = null,
            )

        val program = builder.buildProgramNode(listOf(decl))
        val result = fmt.formatToString(program, FormatConfig())

        assertEquals("let x: number;", result)
    }

    @Test
    fun `assignment with no spaces around equals`() {
        val assign =
            builder.buildAssignmentStatementNode(
                identifier = tok(CommonTypes.IDENTIFIER, "a"),
                value = LiteralExpression(tok(CommonTypes.NUMBER_LITERAL, "42"), Position(0, 0)),
            )

        val config = FormatConfig(spaceAroundAssignment = false)
        val program = builder.buildProgramNode(listOf(assign))
        val result = fmt.formatToString(program, config)

        assertEquals("a=42;", result)
    }

    @Test
    fun `println with blank line before`() {
        val printStmt =
            builder.buildPrintStatementNode(
                builder.buildLiteralExpressionNode(tok(CommonTypes.STRING_LITERAL, "hi")),
            )

        val config = FormatConfig(blankLinesBeforePrintln = 1)
        val program = builder.buildProgramNode(listOf(printStmt))
        val result = fmt.formatToString(program, config)

        assertEquals("\nprintln(\"hi\");", result)
    }

    @Test
    fun `custom colon spacing in declaration`() {
        val decl =
            builder.buildVariableDeclarationStatementNode(
                tok(CommonTypes.LET, "let"),
                identifier = tok(CommonTypes.IDENTIFIER, "msg"),
                dataType = tok(CommonTypes.STRING, "string"),
                initialValue =
                    LiteralExpression(
                        tok(CommonTypes.STRING_LITERAL, "ok"),
                        Position(0, 0),
                    ),
            )

        val config =
            FormatConfig(
                spaceBeforeColon = true,
                spaceAfterColon = false,
                spaceAroundAssignment = true,
            )
        val program = builder.buildProgramNode(listOf(decl))
        val result = fmt.formatToString(program, config)

        assertEquals("let msg :string = \"ok\";", result)
    }

    @Test
    fun `formatToWriter writes declaration with default config`() {
        val decl =
            builder.buildVariableDeclarationStatementNode(
                tok(CommonTypes.LET, "let"),
                identifier = tok(CommonTypes.IDENTIFIER, "x"),
                dataType = tok(CommonTypes.NUMBER, "number"),
                initialValue = null,
            )
        val program = builder.buildProgramNode(listOf(decl))
        val writer = StringWriter()

        fmt.formatToWriter(program, FormatConfig(), writer)

        assertEquals("let x: number;", writer.toString())
    }

    @Test
    fun `formatToWriter writes assignment without spaces around equals`() {
        val assign =
            builder.buildAssignmentStatementNode(
                identifier = tok(CommonTypes.IDENTIFIER, "a"),
                value = LiteralExpression(tok(CommonTypes.NUMBER_LITERAL, "42"), Position(0, 0)),
            )
        val config = FormatConfig(spaceAroundAssignment = false)
        val program = builder.buildProgramNode(listOf(assign))
        val writer = StringWriter()

        fmt.formatToWriter(program, config, writer)

        assertEquals("a=42;", writer.toString())
    }
}
