package formatter.engine

import Token
import formatter.config.FormatConfig

class FormatterContext(
    val config: FormatConfig,
    val out: StringBuilder,
    val indentationManager: IndentationManager = IndentationManager(config),
    val spaceManager: SpaceManager = SpaceManager(config),
) {
    var indentLevel: Int = 0
    var previousToken: Token? = null
    var expectingIfBrace: Boolean = false
    var isPrintlnStatement: Boolean = false
    var newLineAdded: Boolean = true

    var isEndAfterThisToken: () -> Boolean = { false }

    fun ensureIndentBeforeNonClosing(next: Token) {
        newLineAdded =
            indentationManager.ensureIndentBeforeNonClosing(out, indentLevel, next, newLineAdded)
    }

    fun addIndentation() {
        indentationManager.addIndentation(out, indentLevel)
    }

    fun ensureSpaceBetween(current: Token) {
        spaceManager.ensureSpaceBetween(out, previousToken, current)
    }

    fun handleIfBrace() {
        when (config.ifBraceOnSameLine) {
            true -> out.append(' ')
            false -> out.append('\n')
            null -> if (out.isNotEmpty() && !out.last().isWhitespace()) out.append(' ')
        }
    }
}
