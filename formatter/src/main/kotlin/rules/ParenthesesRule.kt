import formatter.engine.FormatterContext
import formatter.rules.FormattingRule
import formatter.util.isDelimiterParentheses
import type.CommonTypes

class ParenthesesRule : FormattingRule {
    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean = isDelimiterParentheses(token)

    override fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext> {
        val value = token.getValue().trim()
        val text = formatParenthesis(value, token, context)
        return Pair(text, context.withoutNewLine())
    }

    private fun formatParenthesis(
        value: String,
        token: Token,
        context: FormatterContext,
    ): String {
        val indentation = getIndentation(context)
        return when (value) {
            "(" -> indentation + getSpaceBeforeOpen(token, context) + value
            ")" -> indentation + getSpaceBeforeClose(context) + value
            else -> indentation + value
        }
    }

    private fun getIndentation(context: FormatterContext): String =
        if (context.newLineAdded) {
            context.indentationManager.getIndentation(context.indentLevel)
        } else {
            ""
        }

    private fun getSpaceBeforeOpen(
        token: Token,
        context: FormatterContext,
    ): String =
        when {
            context.newLineAdded -> ""
            context.previousToken?.getType() == CommonTypes.IDENTIFIER -> ""
            else ->
                context.spaceManager.getSpaceBetween(
                    context.previousToken,
                    token,
                    null,
                )
        }

    private fun getSpaceBeforeClose(context: FormatterContext): String =
        when {
            context.newLineAdded -> ""
            else -> ""
        }
}
