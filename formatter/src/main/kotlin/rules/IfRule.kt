// IfRule.kt
package formatter.rules

import Token
import formatter.engine.FormatterContext
import formatter.rules.token.TokenFormatter
import type.CommonTypes

class IfRule(
    private val tokenFormatter: TokenFormatter,
) : FormattingRule {

    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean =
        token.getType() == CommonTypes.CONDITIONALS &&
            token.getValue().trim() == "if"

    override fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext> {
        val (formattedText, intermediateContext) = tokenFormatter.format(token, context)
        val newContext = intermediateContext.copy(expectingIfBrace = true)
        return Pair(formattedText, newContext)
    }
}
