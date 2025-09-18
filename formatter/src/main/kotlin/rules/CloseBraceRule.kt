// Archivo: formatter/rules/CloseBraceRule.kt (Corregido)
package formatter.rules

import Token
import formatter.engine.FormatterContext

class CloseBraceRule : FormattingRule {
    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean = token.getValue().trim() == "}"

    override fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext> {
        val reindentedContext = context.decreaseIndent()

        val indentation =
            reindentedContext.indentationManager.getIndentation(
                reindentedContext.indentLevel,
            )

        val formattedText = "$indentation}\n"

        return Pair(formattedText, reindentedContext.withNewLine())
    }
}
