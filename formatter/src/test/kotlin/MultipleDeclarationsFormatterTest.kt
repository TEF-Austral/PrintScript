package formatter

import PrintScriptToken
import builder.DefaultNodeBuilder
import formatter.factory.FormatterFactory
import formatter.config.FormatConfig
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import type.CommonTypes
import coordinates.Position

class MultipleDeclarationsFormatterTest {
    private val builder = DefaultNodeBuilder()
    private val fmt = FormatterFactory.createCustom(emptyList())

    private fun tok(
        type: CommonTypes,
        value: String,
    ) = PrintScriptToken(type, value, Position(1, 1))

    @Test
    fun `format multiple declarations with no rules`() {
        val decl1 =
            builder.buildVariableDeclarationStatementNode(
                tok(CommonTypes.LET, "let"),
                identifier = tok(CommonTypes.IDENTIFIER, "something"),
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
                dataType = tok(CommonTypes.STRING, "string"),
                initialValue =
                    builder.buildLiteralExpressionNode(
                        tok(CommonTypes.STRING_LITERAL, "another really cool thing"),
                    ),
            )
        val decl3 =
            builder.buildVariableDeclarationStatementNode(
                tok(CommonTypes.LET, "let"),
                identifier = tok(CommonTypes.IDENTIFIER, "twice_thing"),
                dataType = tok(CommonTypes.STRING, "string"),
                initialValue =
                    builder.buildLiteralExpressionNode(
                        tok(CommonTypes.STRING_LITERAL, "another really cool thing twice"),
                    ),
            )
        val decl4 =
            builder.buildVariableDeclarationStatementNode(
                tok(CommonTypes.LET, "let"),
                identifier = tok(CommonTypes.IDENTIFIER, "third_thing"),
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
            let another_thing:string="another really cool thing";
            let twice_thing:string="another really cool thing twice";
            let third_thing:string="another really cool thing three times";
            """.trimIndent()

        assertEquals(expected, result)
    }
}
