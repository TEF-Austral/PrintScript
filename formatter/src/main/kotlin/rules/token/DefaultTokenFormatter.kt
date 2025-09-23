package formatter.rules.token

import Token
import formatter.engine.FormatterContext
import type.CommonTypes

class DefaultTokenFormatter : TokenFormatter {
    override fun format(
        token: Token,
        context: FormatterContext,
        addSpace: Boolean,
    ): Pair<String, FormatterContext> {
        val indentation =
            if (context.newLineAdded) {
                context.indentationManager.getIndentation(context.indentLevel)
            } else {
                ""
            }

        val space =
            when {
                !addSpace -> ""
                context.newLineAdded -> ""
                context.colonJustProcessed -> ""
                context.previousToken?.getValue()?.contains("println") == true -> ""
                token.getType() == CommonTypes.PRINT -> {
                    context.spaceManager.getSpaceBetween(context.previousToken, token, null)
                }
                else -> context.spaceManager.getSpaceBetween(context.previousToken, token, null)
            }

        val formattedText = indentation + space + token.getValue()
        val newContext = context.withoutNewLine().copy(colonJustProcessed = false)

        return Pair(formattedText, newContext)
    }
}
