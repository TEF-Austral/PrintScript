package formatter.rules

import Token
import formatter.engine.FormatterContext
import formatter.rules.token.TokenFormatter
import formatter.util.isConditionIf

class IfRule(
    private val tokenFormatter: TokenFormatter,
) : FormattingRule {

    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean = isConditionIf(token)

    override fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext> {
        val (formattedText, intermediateContext) = tokenFormatter.format(token, context)
        val newContext = intermediateContext.copy(expectingIfBrace = true)
        return Pair(formattedText, newContext)
    }
}
