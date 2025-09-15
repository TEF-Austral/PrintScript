package formatter

import formatter.config.FormatConfig
import formatter.config.FormatterConstants
import formatter.rules.FormatRule
import formatter.rules.RuleRegistry
import node.Program
import node.PrintStatement
import java.io.Writer

class DefaultFormatter(
    private val rules: List<FormatRule>,
) : Formatter {
    override fun formatToString(
        program: Program,
        config: FormatConfig,
    ): String {
        // if no rules supplied, passthrough raw text + only apply breakAfterStatement
        if (rules.isEmpty()) {
            // Only enforce: newline after “;” and collapse multi-spaces
            val minimalFormatter = DefaultFormatter(RuleRegistry.rulesV10)
            val overrideConfig =
                config.copy(
                    spaceBeforeColon = false,
                    spaceAfterColon = false,
                    spaceAroundAssignment = false,
                    spaceAroundOperators = false,
                    enforceSingleSpace = true,
                    enabledRules = emptySet(),
                )
            return minimalFormatter.formatToString(program, overrideConfig)
        }

        // existing logic
        val sb = StringBuilder()
        val active =
            if (config.enabledRules.isEmpty()) {
                rules
            } else {
                rules.filter { it.id in config.enabledRules }
            }

        val statements = program.getStatements()
        val firstPrintIndex = statements.indexOfFirst { it is PrintStatement }

        statements.forEachIndexed { index, stmt ->
            val effectiveConfig =
                if (stmt is PrintStatement && index == firstPrintIndex) {
                    config
                } else {
                    config.copy(blankLinesAfterPrintln = 0)
                }

            active
                .first { it.matches(stmt) }
                .apply(stmt, sb, effectiveConfig, 0)

            if (!effectiveConfig.breakAfterStatement) sb.setLength(sb.length - 1)
        }

        var result = sb.toString()
        if (config.enforceSingleSpace) {
            result =
                result
                    .lines()
                    .joinToString("\n") { line ->
                        val indent = line.takeWhile { it == ' ' }
                        indent +
                            line
                                .drop(indent.length)
                                .replace(FormatterConstants.MULTI_SPACE_REGEX, " ")
                    }
        }
        return result.removeSuffix("\n")
    }

    override fun formatToWriter(
        program: Program,
        config: FormatConfig,
        writer: Writer,
    ) = writer.write(formatToString(program, config))
}
