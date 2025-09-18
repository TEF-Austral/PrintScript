package formatter.rules

import Token
import formatter.engine.FormatterContext
import formatter.util.isDelimiterSemicolon

class SemicolonRule : FormattingRule {
    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean = isDelimiterSemicolon(token)

    override fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext> {
        var formattedText = ";\n"

        if (context.isPrintlnStatement) {
            val blankLines = context.config.blankLinesAfterPrintln.coerceIn(0, 3)
            formattedText += "\n".repeat(blankLines)
        }

        val newContext = context.withNewLine().copy(isPrintlnStatement = false)
        return Pair(formattedText, newContext)
    }
}
