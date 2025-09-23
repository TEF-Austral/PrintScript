package formatter.rules

import Token
import formatter.engine.FormatterContext
import formatter.util.isDelimiterCloseBrace

class CloseBraceRule : FormattingRule {
    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean = isDelimiterCloseBrace(token)

    override fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext> {
        val reindentedContext = context.decreaseIndent()

        val indentation =
            reindentedContext.indentationManager.getIndentation(
                reindentedContext.indentLevel,
            )

        val formattedText = "$indentation}\n"

        return Pair(formattedText, reindentedContext.withNewLine())
    }
}
