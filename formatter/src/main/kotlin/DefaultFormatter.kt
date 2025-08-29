package formatter

import formatter.config.FormatterConstants.MULTI_SPACE_REGEX
import formatter.config.FormatConfig
import formatter.visitor.ASTFormatterVisitor
import node.Program
import java.io.Writer

class DefaultFormatter : Formatter {
    override fun formatToString(program: Program, config: FormatConfig): String {
        val formatted = ASTFormatterVisitor().visitProgram(program, config)
        return formatted
            .lines()
            .joinToString("\n") { line ->
                val indent = line.takeWhile { it == ' ' }
                val content = line.drop(indent.length)
                    .replace(MULTI_SPACE_REGEX, " ")
                indent + content
            } + "\n"
    }

    override fun formatToWriter(program: Program, config: FormatConfig, writer: Writer) {
        writer.write(formatToString(program, config))
    }
}