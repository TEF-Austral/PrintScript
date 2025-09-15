import builder.DefaultNodeBuilder
import coordinates.Position
import formatter.DefaultFormatter
import formatter.config.FormatConfig
import formatter.rules.RuleId
import formatter.rules.RuleRegistry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import type.CommonTypes

class SpacingOnlyFormatterTest {
    private val builder = DefaultNodeBuilder()
    private val fmt = DefaultFormatter(RuleRegistry.rulesV10)

    private fun tok(
        type: CommonTypes,
        value: String,
    ) = PrintScriptToken(type, value, Position(1, 1))

    @Test
    fun `only assignment spacing does not alter colon spacing`() {
        val decls =
            listOf(
                // no space after colon in the token -> expected no space preserved
                Triple("something", "a really cool thing", "string"),
                // leading space in dataType token -> should be preserved after colon
                Triple("another_thing", "another really cool thing", " string"),
                // leading space in dataType token -> should be preserved after colon
                Triple("twice_thing", "another really cool thing twice", " string"),
                // no space after colon in the token -> expected no space preserved
                Triple("third_thing", "another really cool thing three times", "string"),
            ).map { (ident, value, dataTypeValue) ->
                builder.buildVariableDeclarationStatementNode(
                    tok(CommonTypes.LET, "let"),
                    identifier = tok(CommonTypes.IDENTIFIER, ident),
                    // supply dataType token values that sometimes include a leading space
                    dataType = tok(CommonTypes.STRING, dataTypeValue),
                    initialValue =
                        builder.buildLiteralExpressionNode(
                            tok(CommonTypes.STRING_LITERAL, value),
                        ),
                )
            }

        val program = builder.buildProgramNode(decls)

        // enable only the Assignment rule so only assignment spacing is applied
        val config =
            FormatConfig(
                spaceAroundAssignment = true,
                enabledRules = setOf(RuleId.Assignment),
            )

        val result = fmt.formatToString(program, config)

        val expected =
            """
            let something:string = "a really cool thing";
            let another_thing: string = "another really cool thing";
            let twice_thing: string = "another really cool thing twice";
            let third_thing:string = "another really cool thing three times";
            """.trimIndent()

        assertEquals(expected, result)
    }
}
