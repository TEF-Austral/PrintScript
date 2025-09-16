import builder.DefaultNodeBuilder
import coordinates.Position
import formatter.DefaultFormatter
import formatter.config.FormatConfig
import formatter.rules.RuleRegistry
import node.AssignmentStatement
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import type.CommonTypes

class PreserveSpacingFormatterTest {
    private val builder = DefaultNodeBuilder()
    private val fmt = DefaultFormatter(RuleRegistry.rulesV10)
    private val pos = Position(1, 1)

    private fun tok(
        type: CommonTypes,
        value: String,
    ) = PrintScriptToken(type, value, pos)

    @Test
    fun `only add space after colon and keep any pre-existing space before colon`() {
        // Simulate input that already had a space before ':'
        val decl =
            builder.buildVariableDeclarationStatementNode(
                tok(CommonTypes.LET, "let"),
                identifier = tok(CommonTypes.IDENTIFIER, "name "),
                dataType = tok(CommonTypes.STRING, "string"),
                initialValue = null,
            )

        val config =
            FormatConfig(
                spaceAfterColon = true, // only enforce space after ':'
                spaceBeforeColon = null, // do not force/remove before ':'
            )

        val result = fmt.formatToString(builder.buildProgramNode(listOf(decl)), config)
        assertEquals("let name : string;", result)
    }

    @Test
    fun `preserve original spaces around '=' when unspecified`() {
        val xId = tok(CommonTypes.IDENTIFIER, "x")
        val yId = tok(CommonTypes.IDENTIFIER, "y")
        val zId = tok(CommonTypes.IDENTIFIER, "z")

        val opBoth = tok(CommonTypes.ASSIGNMENT, " = ")
        val opLeft = tok(CommonTypes.ASSIGNMENT, " =")
        val opRight = tok(CommonTypes.ASSIGNMENT, "= ")

        val val1 = builder.buildLiteralExpressionNode(tok(CommonTypes.NUMBER_LITERAL, "1"))
        val val2 = builder.buildLiteralExpressionNode(tok(CommonTypes.NUMBER_LITERAL, "2"))
        val val3 = builder.buildLiteralExpressionNode(tok(CommonTypes.NUMBER_LITERAL, "3"))

        val asg1 = AssignmentStatement(xId, val1, pos, assignmentOperator = opBoth)
        val asg2 = AssignmentStatement(yId, val2, pos, assignmentOperator = opLeft)
        val asg3 = AssignmentStatement(zId, val3, pos, assignmentOperator = opRight)

        val program = builder.buildProgramNode(listOf(asg1, asg2, asg3))
        val result = fmt.formatToString(program, FormatConfig(spaceAfterColon = true))

        val expected =
            """
            x = 1;
            y =2;
            z= 3;
            """.trimIndent()
        assertEquals(expected, result)
    }

    @Test
    fun `do not add spaces around '=' when disabled`() {
        val assign =
            builder.buildAssignmentStatementNode(
                identifier = tok(CommonTypes.IDENTIFIER, "a"),
                value = builder.buildLiteralExpressionNode(tok(CommonTypes.NUMBER_LITERAL, "42")),
            )
        val config = FormatConfig(spaceAroundAssignment = null)
        val result = fmt.formatToString(builder.buildProgramNode(listOf(assign)), config)
        assertEquals("a=42;", result)
    }
}
