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
        val indentation = getIndentation(context)
        val formattedToken = formatToken(token, context)
        val formattedText = indentation + formattedToken
        return Pair(formattedText, updateContext(context))
    }

    private fun getIndentation(context: FormatterContext): String =
        if (context.newLineAdded) {
            context.indentationManager.getIndentation(context.indentLevel)
        } else {
            ""
        }

    private fun formatToken(
        token: Token,
        context: FormatterContext,
    ): String = formatSpaceAroundAssignment(token, context)

    private fun formatSpaceAroundAssignment(
        token: Token,
        context: FormatterContext,
    ): String =
        when (context.config.spaceAroundAssignment) {
            true -> " " + token.getValue().replace(" ", "") + " "
            false -> token.getValue().replace(" ", "")
            null -> token.getValue()
        }

    private fun updateContext(context: FormatterContext): FormatterContext =
        context.withoutNewLine().copy(colonJustProcessed = false)
}
