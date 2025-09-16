package formatter.engine

import formatter.config.FormatConfig
import formatter.config.FormatterConstants

class DefaultLinePostProcessor : LinePostProcessor {
    override fun process(
        text: String,
        config: FormatConfig,
    ): String {
        // Normalize multi spaces per line but preserve leading indentation
        val normalized =
            buildString {
                val lines = text.lines()
                lines.forEachIndexed { idx, line ->
                    val trimmed = line.trimStart()
                    val indent = line.length - trimmed.length
                    val processed = trimmed.replace(FormatterConstants.MULTI_SPACE_REGEX, " ")
                    append(" ".repeat(indent))
                    append(processed)
                    if (idx < lines.lastIndex) append('\n')
                }
            }

        // Collapse trailing blank lines to at most one newline
        val sb = StringBuilder(normalized)
        while (sb.isNotEmpty() &&
            sb.last() == '\n' &&
            sb.length > 1 &&
            sb[sb.lastIndex - 1] == '\n'
        ) {
            sb.deleteCharAt(sb.lastIndex)
        }
        return sb.toString()
    }
}
