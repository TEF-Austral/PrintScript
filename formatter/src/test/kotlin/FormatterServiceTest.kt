import builder.DefaultNodeBuilder
import coordinates.Position
import formatter.FormatterService
import node.LiteralExpression
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import type.CommonTypes
import java.io.StringWriter
import java.nio.file.Files
import java.nio.file.Path

class FormatterServiceTest {
    @TempDir
    lateinit var tempDir: Path

    private val builder = DefaultNodeBuilder()
    private val service = FormatterService()

    private fun tok(type: CommonTypes, value: String) =
        PrintScriptToken(type, value, Position(1, 1))

    @Test
    fun `formatToString loads json config and formats assignment`() {
        val assign = builder.buildAssignmentStatementNode(
            identifier = tok(CommonTypes.IDENTIFIER, "a"),
            value = LiteralExpression(tok(CommonTypes.NUMBER_LITERAL, "42"))
        )
        val program = builder.buildProgramNode(listOf(assign))

        val configFile = tempDir.resolve("config.json")
        val json = """
            {
              "spaceAroundAssignment": "false"
            }
        """.trimIndent()
        Files.writeString(configFile, json)

        val result = service.formatToString(program, configFile.toString())
        assertEquals("a=42;\n\n", result)
    }

    @Test
    fun `formatToWriter loads yaml config and writes println with blank line`() {
        val printStmt = builder.buildPrintStatementNode(
            builder.buildLiteralExpressionNode(tok(CommonTypes.STRING_LITERAL, "\"hi\""))
        )
        val program = builder.buildProgramNode(listOf(printStmt))

        val configFile = tempDir.resolve("settings.yml")
        val yaml = """
            blankLinesBeforePrintln: 1
        """.trimIndent()
        Files.writeString(configFile, yaml)

        val writer = StringWriter()
        service.formatToWriter(program, configFile.toString(), writer)
        assertEquals("\nprintln(\"hi\");\n\n", writer.toString())
    }
}
