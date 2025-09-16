package formatter.engine

import Token
import formatter.config.FormatConfig

class IndentationManager(
    private val config: FormatConfig,
) {
    fun addIndentation(
        out: StringBuilder,
        indentLevel: Int,
    ) {
        val spacesPerLevel = config.indentSize
        repeat(indentLevel * spacesPerLevel) { out.append(' ') }
    }

    fun ensureIndentBeforeNonClosing(
        out: StringBuilder,
        indentLevel: Int,
        next: Token,
        newLineAdded: Boolean,
    ): Boolean {
        val isClosingBrace = next.getValue().trim() == "}"
        if (newLineAdded && !isClosingBrace) {
            addIndentation(out, indentLevel)
            return false
        }
        return newLineAdded
    }
}
