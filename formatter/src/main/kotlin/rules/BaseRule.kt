import formatter.engine.FormatterContext
import formatter.rules.FormattingRule
import type.CommonTypes

abstract class BaseRule : FormattingRule {
    protected fun format(
        token: Token,
        context: FormatterContext,
        addSpace: Boolean = true,
    ): Pair<String, FormatterContext> {
        val indentation =
            if (context.newLineAdded) {
                context.indentationManager.getIndentation(context.indentLevel)
            } else {
                ""
            }

        // Only add space if appropriate
        val space =
            when {
                !addSpace -> ""
                context.newLineAdded -> ""
                context.colonJustProcessed -> "" // Don't add space after colon
                // Special case: println or other function calls shouldn't have space before (
                context.previousToken?.getValue()?.contains("println") == true -> ""
                token.getType() == CommonTypes.PRINT -> {
                    // For print/println tokens, use normal spacing unless followed by (
                    context.spaceManager.getSpaceBetween(context.previousToken, token, null)
                }
                else -> context.spaceManager.getSpaceBetween(context.previousToken, token, null)
            }

        val formattedText = indentation + space + token.getValue()

        // Clear colonJustProcessed flag
        val newContext = context.withoutNewLine().copy(colonJustProcessed = false)
        return Pair(formattedText, newContext)
    }
}
