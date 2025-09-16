package formatter.config

data class FormatConfig(
    val spaceBeforeColon: Boolean? = null,
    val spaceAfterColon: Boolean? = null,
    val spaceAroundAssignment: Boolean? = null,
    val spaceAroundOperators: Boolean? = null,
    val enforceSingleSpace: Boolean? = null,
    val breakAfterStatement: Boolean? = null,
    val blankLinesAfterPrintln: Int = 0,
    val indentSize: Int = DEFAULT_SIZE,
    val ifBraceOnSameLine: Boolean? = null,
) {
    companion object {
        const val DEFAULT_SIZE: Int = 2
    }
}
