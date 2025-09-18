package formatter.rules

import PrintScriptToken
import Token
import formatter.engine.FormatterContext
import formatter.rules.token.TokenFormatter
import type.CommonTypes

class StringLiteralRule(
    private val tokenFormatter: TokenFormatter,
) : FormattingRule {

    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean = token.getType() == CommonTypes.STRING_LITERAL

    override fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext> {
        val valueWithQuotes = "\"${token.getValue()}\""
        val tempToken =
            PrintScriptToken(
                token.getType(),
                valueWithQuotes,
                token.getCoordinates(),
            )

        return tokenFormatter.format(tempToken, context)
    }
}
