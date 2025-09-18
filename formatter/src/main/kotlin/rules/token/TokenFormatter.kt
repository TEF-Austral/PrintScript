package formatter.rules.token

import Token
import formatter.engine.FormatterContext

interface TokenFormatter {
    fun format(
        token: Token,
        context: FormatterContext,
        addSpace: Boolean = true,
    ): Pair<String, FormatterContext>
}
