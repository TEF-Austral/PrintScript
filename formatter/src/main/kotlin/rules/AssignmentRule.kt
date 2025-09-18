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

        val token =
            when (context.config.spaceAroundAssignment) {
                true -> " " + token.getValue().replace(" ", "") + " "
                false -> token.getValue().replace(" ", "")
                null -> token.getValue()
            }

        val tokenEnforcedSingleSpace =
            when (context.config.enforceSingleSpace) {
                true -> " " + token.replace(" ", "") + " "
                false -> token.replace(" ", "")
                null -> token
            }

        val formattedText = indentation + tokenEnforcedSingleSpace
        return Pair(formattedText, context.withoutNewLine().copy(colonJustProcessed = false))
    }
}
