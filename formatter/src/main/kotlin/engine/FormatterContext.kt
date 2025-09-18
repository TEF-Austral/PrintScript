package formatter.engine

import SpaceManager
import Token
import formatter.config.FormatConfig

data class FormatterContext(
    val config: FormatConfig,
    val indentLevel: Int = 0,
    val previousToken: Token? = null,
    val newLineAdded: Boolean = true,
    val isPrintlnStatement: Boolean = false,
    val expectingIfBrace: Boolean = false,
    val colonJustProcessed: Boolean = false,
) {
    val indentationManager: IndentationManager = IndentationManager(config)
    val spaceManager: SpaceManager = SpaceManager(config)

    fun increaseIndent(): FormatterContext = this.copy(indentLevel = indentLevel + 1)

    fun decreaseIndent(): FormatterContext {
        val newIndentLevel = if (indentLevel > 0) indentLevel - 1 else 0
        return this.copy(indentLevel = newIndentLevel)
    }

    fun withNewLine(): FormatterContext = this.copy(newLineAdded = true)

    fun withoutNewLine(): FormatterContext = this.copy(newLineAdded = false)
}
