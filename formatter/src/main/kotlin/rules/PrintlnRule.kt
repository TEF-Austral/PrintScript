package formatter.rules

import BaseRule
import Token
import formatter.engine.FormatterContext
import type.CommonTypes

class PrintlnRule : BaseRule() {
    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean = token.getType() == CommonTypes.PRINT && token.getValue().contains("println")

    override fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext> {
        val (formattedText, intermediateContext) = format(token, context)

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
