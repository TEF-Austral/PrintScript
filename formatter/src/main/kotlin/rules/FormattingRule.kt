package formatter.rules

import Token
import formatter.engine.FormatterContext

interface FormattingRule {

    fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean

    fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext>
}
