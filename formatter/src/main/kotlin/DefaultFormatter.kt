package formatter

import formatter.config.FormatConfig
import formatter.config.FormatterConstants.MULTI_SPACE_REGEX
import formatter.rules.RuleRegistry
import node.ASTNode
import node.Program
import java.io.Writer

class DefaultFormatter : Formatter {
    override fun formatToString(
        program: Program,
        config: FormatConfig,
    ): String {
        val sb = StringBuilder()
        program.getStatements().forEach { stmt ->
            RuleRegistry.rules
                .first { it.matches(stmt as ASTNode) }
                .apply(stmt, sb, config, 0)
        }
        return sb
            .toString()
            .lines()
            .joinToString("\n") { line ->
                val indent = line.takeWhile { it == ' ' }
                val content = line.drop(indent.length).replace(MULTI_SPACE_REGEX, " ")
                indent + content
            } + "\n"
    }

    override fun formatToWriter(
        program: Program,
        config: FormatConfig,
        writer: Writer,
    ) {
        writer.write(formatToString(program, config))
    }
}
