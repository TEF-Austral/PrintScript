package formatter.config

import formatter.rules.RuleId

data class FormatConfig(
    val spaceBeforeColon: Boolean = false,
    val spaceAfterColon: Boolean = false,
    val spaceAroundAssignment: Boolean = false,
    val spaceAroundOperators: Boolean = false,
    val enforceSingleSpace: Boolean = false,
    val breakAfterStatement: Boolean = true,
    val blankLinesAfterPrintln: Int = 0,
    val indentSize: Int = DEFAULT_INDENT_SIZE,
    val ifBraceOnSameLine: Boolean = true,
    val ifIndentInside: Int = DEFAULT_IF_INDENT_INSIDE,
    // new flag, empty = all rules enabled
    val enabledRules: Set<RuleId> = emptySet(),
) {
    companion object {
        const val DEFAULT_INDENT_SIZE: Int = 4
        const val DEFAULT_IF_INDENT_INSIDE: Int = 1
    }
}
