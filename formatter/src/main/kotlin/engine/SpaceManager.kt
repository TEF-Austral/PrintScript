package formatter.engine

import Token
import formatter.config.FormatConfig
import type.CommonTypes

class SpaceManager(
    private val config: FormatConfig,
) {
    fun ensureSpaceBetween(
        out: StringBuilder,
        previousToken: Token?,
        current: Token,
    ) {
        val prev = previousToken ?: return

        if (isEnforceSingleSpace()) {
            appendSingleSpaceIfNeeded(out)
            return
        }

        val prevTrim = trimmedValue(prev)
        val curTrim = trimmedValue(current)

        if (!shouldSkipSpace(prevTrim, curTrim, prev, current) && lastCharNotWhitespace(out)) {
            out.append(' ')
        }
    }

    private fun isEnforceSingleSpace(): Boolean = config.enforceSingleSpace == true

    private fun appendSingleSpaceIfNeeded(out: StringBuilder) {
        if (out.isNotEmpty() && !out.last().isWhitespace()) {
            out.append(' ')
        }
    }

    private fun trimmedValue(token: Token): String = token.getValue().trim()

    private fun shouldSkipSpace(
        prevTrim: String,
        curTrim: String,
        prev: Token,
        current: Token,
    ): Boolean =
        when {
            prevTrim == "(" -> true
            curTrim == ")" -> true
            curTrim == ";" -> true
            curTrim == ":" -> true
            current.getType() == CommonTypes.ASSIGNMENT -> true
            prev.getType() == CommonTypes.PRINT && curTrim == "(" -> true
            else -> false
        }

    private fun lastCharNotWhitespace(out: StringBuilder): Boolean =
        out.isNotEmpty() && !out.last().isWhitespace()
}
