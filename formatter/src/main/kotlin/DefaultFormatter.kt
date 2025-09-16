package formatter

import formatter.config.FormatConfig
import formatter.config.FormatterConstants
import formatter.rules.FormatRule
import formatter.rules.RuleId
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
        // always‐on RuleIds
        val defaults =
            setOf(
                RuleId.Declaration,
                RuleId.Assignment,
                RuleId.ExpressionStatement,
                RuleId.PrintStatement,
                RuleId.EmptyStatement,
                RuleId.BinaryExpression,
                RuleId.StringLiteralExpression,
                RuleId.LiteralExpression,
                RuleId.IdentifierExpression,
                RuleId.IfStatement,
                RuleId.ReadInputExpression,
                RuleId.ReadEnvExpression,
            )

        // if no custom flags, use all rules; otherwise defaults + enabled
        val active =
            if (config.enabledRules.isEmpty()) {
                rules
            } else {
                rules.filter { it.id in defaults || it.id in config.enabledRules }
            }

        val sb = StringBuilder()
        val stmts = program.getStatements()
        val firstPrint = stmts.indexOfFirst { it is PrintStatement }

        stmts.forEachIndexed { idx, stmt ->
            val cfg =
                if (stmt is PrintStatement && idx == firstPrint) {
                    config
                } else {
                    config.copy(blankLinesAfterPrintln = 0)
                }

            active
                .first { it.matches(stmt) }
                .apply(stmt, sb, cfg, 0)

            // only remove the trailing newline/semicolon when flag is explicitly false
            if (cfg.breakAfterStatement == false) sb.setLength(sb.length - 1)
        }

        var result = sb.toString()
        if (config.enforceSingleSpace == true) {
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
