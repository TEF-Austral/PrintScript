import formatter.engine.FormatterContext
import formatter.rules.FormattingRule
import type.CommonTypes

class DefaultTokenRule : FormattingRule {
    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean = true

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

        // Check if we should add space
        val space =
            when {
                context.newLineAdded -> ""
                context.colonJustProcessed -> "" // Colon handles its own spacing
                // Check if current token is opening parenthesis after function name
                token.getValue().trim() == "(" &&
                    context.previousToken?.getType() == CommonTypes.IDENTIFIER -> ""
                // Apply enforceSingleSpace if configured
                context.config.enforceSingleSpace == true -> {
                    // Don't add space before certain tokens even with enforceSingleSpace
                    if (token.getValue().trim() in listOf(")", ";", "(")) "" else " "
                }
                else -> context.spaceManager.getSpaceBetween(context.previousToken, token, null)
            }

        val formattedText = indentation + space + token.getValue()

        // Clear colonJustProcessed flag
        val newContext = context.withoutNewLine().copy(colonJustProcessed = false)
        return Pair(formattedText, newContext)
    }
}
