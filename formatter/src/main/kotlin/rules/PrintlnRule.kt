package formatter.rules

import Token
import formatter.engine.FormatterContext
import formatter.rules.token.TokenFormatter
import type.CommonTypes

class PrintlnRule(
    private val tokenFormatter: TokenFormatter,
) : FormattingRule {

    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean =
        token.getType() == CommonTypes.PRINT &&
            token.getValue().contains("println")

    override fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext> {
        val (formattedText, intermediateContext) = tokenFormatter.format(token, context)

        val finalText =
            when (intermediateContext.config.enforceSingleSpace) {
                true -> formattedText.trimEnd() + " "
                false -> formattedText.replace(" ", "")
                else -> formattedText
            }

        val newContext = intermediateContext.copy(isPrintlnStatement = true)
        return Pair(finalText, newContext)
    }
}
