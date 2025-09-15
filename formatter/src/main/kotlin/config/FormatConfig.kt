// formatter/src/main/kotlin/config/FormatConfig.kt
package formatter.config

data class FormatConfig(
    val spaceBeforeColon: Boolean = false,
    val spaceAfterColon: Boolean = true,
    val spaceAroundAssignment: Boolean = true,
    val spaceAroundOperators: Boolean = true,
    val enforceSingleSpace: Boolean = true,
    val breakAfterStatement: Boolean = true,
    val blankLinesAfterPrintln: Int = 0,
    val indentSize: Int = DEFAULT_INDENT_SIZE,
    val ifBraceOnSameLine: Boolean = true,
    val ifIndentInside: Int = DEFAULT_IF_INDENT_INSIDE,
) {
    companion object {
        const val DEFAULT_INDENT_SIZE: Int = 4
        const val DEFAULT_IF_INDENT_INSIDE: Int = 1
    }
}
