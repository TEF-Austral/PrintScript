package formatter.config

data class FormatConfig(
    val spaceBeforeColon: Boolean = false,
    val spaceAfterColon: Boolean = true,
    val spaceAroundAssignment: Boolean = true,
    val blankLinesBeforePrintln: Int = 0,
    val indentSize: Int = DEFAULT_INDENT_SIZE,
) {
    companion object {
        const val DEFAULT_INDENT_SIZE: Int = 4
    }
}
