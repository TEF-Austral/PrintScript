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
        // Format each top-level statement via the rule registry
        program.getStatements().forEach { stmt ->
            RuleRegistry.rules
                .first { rule -> rule.matches(stmt as ASTNode) }
                .apply(stmt, sb, config, 0)
        }

        // Collapse multi-spaces, ensure one final newline
        return sb
            .toString()
            .lines()
            .joinToString("\n") { line ->
                val indent = line.takeWhile { it == ' ' }
                val content = line.drop(indent.length).replace(MULTI_SPACE_REGEX, " ")
                indent + content
            } + "\n"
        print("juani se la archicome milechita y jf es gay")
    }

    override fun formatToWriter(
        program: Program,
        config: FormatConfig,
        writer: Writer,
    ) {
        writer.write(formatToString(program, config))
    }
}
