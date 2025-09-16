import builder.DefaultNodeBuilder
import coordinates.Position
import formatter.DefaultFormatter
import formatter.config.FormatConfig
import formatter.rules.RuleRegistry
import node.AssignmentStatement
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import type.CommonTypes

class MultipleDeclarationsFormatterTest {
    private val builder = DefaultNodeBuilder()
    private val fmt = DefaultFormatter(RuleRegistry.rulesV10)

    private fun tok(
        type: CommonTypes,
        value: String,
    ) = PrintScriptToken(type, value, Position(1, 1))

    @Test
    fun `format multiple declarations preserves original spacing when config is null`() {
        val decl1 =
            builder.buildVariableDeclarationStatementNode(
                tok(CommonTypes.LET, "let"),
                identifier = tok(CommonTypes.IDENTIFIER, "something"),
                // no leading space in dataType -> no space after colon
                dataType = tok(CommonTypes.STRING, "string"),
                initialValue =
                    builder.buildLiteralExpressionNode(
                        tok(CommonTypes.STRING_LITERAL, "a really cool thing"),
                    ),
            )
        val decl2 =
            builder.buildVariableDeclarationStatementNode(
                tok(CommonTypes.LET, "let"),
                identifier = tok(CommonTypes.IDENTIFIER, "another_thing"),
                // leading space in dataType -> preserved as a space after colon
                dataType = tok(CommonTypes.STRING, " string"),
                initialValue =
                    builder.buildLiteralExpressionNode(
                        tok(CommonTypes.STRING_LITERAL, "another really cool thing"),
                    ),
            )
        val decl3 =
            builder.buildVariableDeclarationStatementNode(
                tok(CommonTypes.LET, "let"),
                identifier = tok(CommonTypes.IDENTIFIER, "twice_thing"),
                // leading space in dataType -> preserved as a space after colon
                dataType = tok(CommonTypes.STRING, " string"),
                initialValue =
                    builder.buildLiteralExpressionNode(
                        tok(CommonTypes.STRING_LITERAL, "another really cool thing twice"),
                    ),
            )
        val decl4 =
            builder.buildVariableDeclarationStatementNode(
                tok(CommonTypes.LET, "let"),
                identifier = tok(CommonTypes.IDENTIFIER, "third_thing"),
                // no leading space in dataType -> no space after colon
                dataType = tok(CommonTypes.STRING, "string"),
                initialValue =
                    builder.buildLiteralExpressionNode(
                        tok(CommonTypes.STRING_LITERAL, "another really cool thing three times"),
                    ),
            )

        val program = builder.buildProgramNode(listOf(decl1, decl2, decl3, decl4))
        val result = fmt.formatToString(program, FormatConfig())

        val expected =
            """
            let something:string="a really cool thing";
            let another_thing: string="another really cool thing";
            let twice_thing: string="another really cool thing twice";
            let third_thing:string="another really cool thing three times";
            """.trimIndent()

        assertEquals(expected, result)
    }

    @Test
    fun `format assignments preserves original '=' spacing when config is null`() {
        val xId = tok(CommonTypes.IDENTIFIER, "x")
        val yId = tok(CommonTypes.IDENTIFIER, "y")
        val zId = tok(CommonTypes.IDENTIFIER, "z")

        val opSpaceBoth = tok(CommonTypes.ASSIGNMENT, " = ")
        val opSpaceLeft = tok(CommonTypes.ASSIGNMENT, " =")
        val opSpaceRight = tok(CommonTypes.ASSIGNMENT, "= ")

        val val1 = builder.buildLiteralExpressionNode(tok(CommonTypes.NUMBER_LITERAL, "1"))
        val val2 = builder.buildLiteralExpressionNode(tok(CommonTypes.NUMBER_LITERAL, "2"))
        val val3 = builder.buildLiteralExpressionNode(tok(CommonTypes.NUMBER_LITERAL, "3"))

        val asg1 = AssignmentStatement(xId, val1, Position(1, 1), assignmentOperator = opSpaceBoth)
        val asg2 = AssignmentStatement(yId, val2, Position(1, 1), assignmentOperator = opSpaceLeft)
        val asg3 = AssignmentStatement(zId, val3, Position(1, 1), assignmentOperator = opSpaceRight)

        val program = builder.buildProgramNode(listOf(asg1, asg2, asg3))
        val result = fmt.formatToString(program, FormatConfig())

        val expected =
            """
            x = 1;
            y =2;
            z= 3;
            """.trimIndent()

        assertEquals(expected, result)
    }
}
