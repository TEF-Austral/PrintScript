package formatter.rules

data class FormatState(
    val indentationLevel: Int = 0,
    val isNewLine: Boolean = true
)