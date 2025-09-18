package formatter.rules

import Token
import formatter.engine.FormatterContext

class OpenBraceRule : FormattingRule {
    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean = token.getValue().trim() == "{"

    override fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext> {
        val formattedText: String

        if (context.expectingIfBrace && context.config.ifBraceOnSameLine == true) {
            formattedText = " {\n"
        } else {
            val indentation = context.indentationManager.getIndentation(context.indentLevel)
            formattedText = "\n" + indentation + "{\n"
        }

        val newContext = context.increaseIndent().withNewLine().copy(expectingIfBrace = false)
        return Pair(formattedText, newContext)
    }
}
