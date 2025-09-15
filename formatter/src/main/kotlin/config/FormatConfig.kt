package formatter.config

import formatter.rules.RuleId

data class FormatConfig(
    val spaceBeforeColon: Boolean? = null,
    val spaceAfterColon: Boolean? = null,
    val spaceAroundAssignment: Boolean? = null,
    val spaceAroundOperators: Boolean? = null,
    val enforceSingleSpace: Boolean? = null,
    val breakAfterStatement: Boolean? = null,
    val blankLinesAfterPrintln: Int = 0,
    val indentSize: Int = DEFAULT_INDENT_SIZE,
    val ifBraceOnSameLine: Boolean? = null,
    val ifIndentInside: Int = DEFAULT_IF_INDENT_INSIDE,
    // new flag, empty = all rules enabled
    val enabledRules: Set<RuleId> = emptySet(),
) {
    companion object {
        const val DEFAULT_INDENT_SIZE: Int = 4
        const val DEFAULT_IF_INDENT_INSIDE: Int = 1
    }
}
