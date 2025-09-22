package formatter.rules

import Token
import formatter.engine.FormatterContext
import formatter.rules.token.TokenFormatter
import formatter.util.isPrintln

class PrintlnRule(
    private val tokenFormatter: TokenFormatter,
) : FormattingRule {

    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean = isPrintln(token)

    override fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext> {
        val (formattedText, intermediateContext) = tokenFormatter.format(token, context)

        val finalText = formattedText

        val newContext = intermediateContext.copy(isPrintlnStatement = true)
        return Pair(finalText, newContext)
    }
}
