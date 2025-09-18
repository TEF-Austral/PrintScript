// Archivo: formatter/rules/IfRule.kt
package formatter.rules

import BaseRule
import Token
import formatter.engine.FormatterContext
import type.CommonTypes

class IfRule : BaseRule() { // Hereda de BaseRule
    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean = token.getType() == CommonTypes.CONDITIONALS && token.getValue().trim() == "if"

    override fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext> {
        val (formattedText, intermediateContext) = format(token, context)
        val newContext = intermediateContext.copy(expectingIfBrace = true)
        return Pair(formattedText, newContext)
    }
}
