import formatter.engine.FormatterContext
import formatter.rules.FormattingRule
import formatter.util.isAssignment

class AssignmentRule : FormattingRule {
    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean = isAssignment(token)

    override fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext> {
        val indentation =
            if (context.newLineAdded) {
                context.indentationManager.getIndentation(context.indentLevel)
            } else {
                ""
            }

        // When config is null, preserve original spacing (default to having space)
        val space =
            when (context.config.spaceAroundAssignment) {
                true -> " "
                false -> ""
                null -> " " // Default: space around assignment
            }

        val formattedText = indentation + space + "=" + space

        // Clear colonJustProcessed flag
        return Pair(formattedText, context.withoutNewLine().copy(colonJustProcessed = false))
    }
}
