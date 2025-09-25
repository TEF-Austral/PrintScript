package formatter.engine

import formatter.config.FormatConfig

class IndentationManager(
    private val config: FormatConfig,
) {

    fun getIndentation(indentLevel: Int): String {
        val spacesPerLevel = config.indentSize
        val totalSpaces = if (indentLevel > 0) indentLevel * spacesPerLevel else 0
        return " ".repeat(totalSpaces)
    }
}
