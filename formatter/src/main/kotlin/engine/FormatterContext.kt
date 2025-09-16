package formatter.engine

import Token
import formatter.config.FormatConfig
import type.CommonTypes

class FormatterContext(
    val config: FormatConfig,
    val out: StringBuilder,
) {
    var indentLevel: Int = 0
    var previousToken: Token? = null
    var expectingIfBrace: Boolean = false
    var isPrintlnStatement: Boolean = false
    var newLineAdded: Boolean = true

    lateinit var isEndAfterThisToken: () -> Boolean

    fun ensureIndentBeforeNonClosing(next: Token) {
        val isClosingBrace = next.getValue().trim() == "}"
        if (newLineAdded && !isClosingBrace) {
            addIndentation()
            newLineAdded = false
        }
    }

    fun addIndentation() {
        val spacesPerLevel = config.indentSize
        repeat(indentLevel * spacesPerLevel) { out.append(' ') }
    }

    fun ensureSpaceBetween(current: Token) {
        val prev = previousToken ?: return

        if (config.enforceSingleSpace == true) {
            if (out.isNotEmpty() && !out.last().isWhitespace()) {
                out.append(' ')
            }
            return
        }

        val prevTrim = prev.getValue().trim()
        val curTrim = current.getValue().trim()

        val skipSpace =
            when {
                prevTrim == "(" -> true
                curTrim == ")" -> true
                curTrim == ";" -> true
                curTrim == ":" -> true
                current.getType() == CommonTypes.ASSIGNMENT -> true
                prev.getType() == CommonTypes.PRINT && curTrim == "(" -> true
                else -> false
            }

        if (!skipSpace && out.isNotEmpty() && !out.last().isWhitespace()) {
            out.append(' ')
        }
    }

    fun handleIfBrace() {
        when (config.ifBraceOnSameLine) {
            true -> out.append(' ')
            false -> out.append('\n')
            null -> if (out.isNotEmpty() && !out.last().isWhitespace()) out.append(' ')
        }
    }
}
