package formatter.engine

import formatter.config.FormatConfig
import formatter.config.FormatterConstants

class DefaultLinePostProcessor : LinePostProcessor {

    override fun process(
        text: String,
        config: FormatConfig,
    ): String {
        val normalized = normalizeLines(text)
        val sb = StringBuilder(normalized)
        collapseMultipleTrailingNewlines(sb)
        return sb.toString()
    }

    private fun normalizeLines(text: String): String =
        buildString {
            val lines = text.lines()
            lines.forEachIndexed { idx, line ->
                append(processLine(line))
                if (idx < lines.lastIndex) append('\n')
            }
        }

    private fun processLine(line: String): String {
        val trimmedStart = line.trimStart()
        val indent = line.length - trimmedStart.length
        val singleSpaced = trimmedStart.replace(FormatterConstants.MULTI_SPACE_REGEX, " ")
        return " ".repeat(indent) + singleSpaced
    }

    private fun collapseMultipleTrailingNewlines(sb: StringBuilder) {
        while (sb.isNotEmpty() &&
            sb.last() == '\n' &&
            sb.length > 1 &&
            sb[sb.lastIndex - 1] == '\n'
        ) {
            sb.deleteCharAt(sb.lastIndex)
        }
    }
}
