package formatter.rules

import Token
import formatter.engine.FormatterContext
import type.CommonTypes

class OperatorRule : FormattingRule {
    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean = token.getType() == CommonTypes.OPERATORS

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
            when (context.config.spaceAroundOperators) {
                true -> " "
                false -> ""
                null -> " "
            }

        val formattedText = indentation + space + token.getValue() + space
        return Pair(formattedText, context.withoutNewLine())
    }
}
