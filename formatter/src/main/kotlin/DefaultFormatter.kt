package formatter

import formatter.config.FormatConfig
import formatter.rules.FormatRule
import formatter.rules.RuleRegistry
import node.Program
import java.io.Writer

class DefaultFormatter(
    private val rules: List<FormatRule>,
) : Formatter {
    constructor() : this(RuleRegistry.rulesV10)

    override fun formatToString(
        program: Program,
        config: FormatConfig,
    ): String {
        val sb = StringBuilder()
        program.getStatements().forEach { stmt ->
            rules
                .first { it.matches(stmt) }
                .apply(stmt, sb, config, 0)
        }
        val result =
            sb
                .toString()
                .lines()
                .joinToString("\n") { line ->
                    val indent = line.takeWhile { it == ' ' }
                    indent +
                            line
                                .drop(indent.length)
                                .replace(formatter.config.FormatterConstants.MULTI_SPACE_REGEX, " ")
                }
        return result.removeSuffix("\n")
    }

    override fun formatToWriter(
        program: Program,
        config: FormatConfig,
        writer: Writer,
    ) {
        writer.write(formatToString(program, config))
    }
}