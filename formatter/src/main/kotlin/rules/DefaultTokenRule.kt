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
        val indentation = indentationIfNewLine(context)
        val space = computeSpace(token, context)
        val formatted = formatToken(indentation, space, token)
        val updatedContext = resetNewLineAndColonFlags(context)
        return Pair(formatted, updatedContext)
    }

    private fun indentationIfNewLine(context: FormatterContext): String =
        if (context.newLineAdded) {
            context.indentationManager.getIndentation(
                context.indentLevel,
            )
        } else {
            ""
        }

    private fun computeSpace(
        token: Token,
        context: FormatterContext,
    ): String {
        if (shouldSkipSpaceBecauseOfNewLine(context)) return ""
        if (shouldSkipSpaceBecauseOfColon(context)) return ""
        val valueTrim = token.getValue().trim()
        if (isFunctionCallOpenParen(valueTrim, context)) return ""
        return context.spaceManager.getSpaceBetween(context.previousToken, token, null)
    }

    private fun shouldSkipSpaceBecauseOfNewLine(context: FormatterContext): Boolean =
        context.newLineAdded

    private fun shouldSkipSpaceBecauseOfColon(context: FormatterContext): Boolean =
        context.colonJustProcessed

    private fun isFunctionCallOpenParen(
        valueTrim: String,
        context: FormatterContext,
    ): Boolean = valueTrim == "(" && context.previousToken?.getType() == CommonTypes.IDENTIFIER

    private fun formatToken(
        indentation: String,
        space: String,
        token: Token,
    ): String = indentation + space + token.getValue()

    private fun resetNewLineAndColonFlags(context: FormatterContext): FormatterContext =
        context.withoutNewLine().copy(colonJustProcessed = false)
}
