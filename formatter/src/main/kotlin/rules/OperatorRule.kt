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

        // When config is null, preserve original spacing (default to having space)
        val space =
            when (context.config.spaceAroundOperators) {
                true -> " "
                false -> ""
                null -> " " // Default: space around operators
            }

        val formattedText = indentation + space + token.getValue() + space
        return Pair(formattedText, context.withoutNewLine())
    }
}
