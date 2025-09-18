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

        val space =
            when {
                context.newLineAdded -> ""
                context.colonJustProcessed -> ""

                token.getValue().trim() == "(" &&
                    context.previousToken?.getType() == CommonTypes.IDENTIFIER -> ""

                context.config.enforceSingleSpace == true -> {
                    if (token.getValue().trim() in listOf(")", ";", "(")) "" else " "
                }
                else -> context.spaceManager.getSpaceBetween(context.previousToken, token, null)
            }

        val formattedText = indentation + space + token.getValue()

        val newContext = context.withoutNewLine().copy(colonJustProcessed = false)
        return Pair(formattedText, newContext)
    }
}
