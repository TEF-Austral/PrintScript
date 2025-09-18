package formatter.util

object SpaceUtil {
    fun splitAround(
        raw: String,
        symbol: String,
    ): Pair<String, String> {
        val idx = raw.indexOf(symbol)
        if (idx == -1) return "" to ""
        val before = raw.substring(0, idx)
        val after = raw.substring(idx + symbol.length)
        return before to after
    }

    fun rebuild(
        raw: String,
        symbol: String,
        spaceBefore: Boolean?,
        spaceAfter: Boolean?,
    ): String {
        val (rawBefore, rawAfter) = splitAround(raw, symbol)
        val before =
            when (spaceBefore) {
                true -> " "
                false -> ""
                null -> rawBefore
            }
        val after =
            when (spaceAfter) {
                true -> " "
                false -> ""
                null -> rawAfter
            }
        return before + symbol + after
    }
}
