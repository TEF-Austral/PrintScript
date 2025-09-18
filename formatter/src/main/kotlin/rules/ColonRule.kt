import formatter.engine.FormatterContext
import formatter.rules.FormattingRule
import formatter.util.isDelimiterColon

class ColonRule : FormattingRule {
    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean = isDelimiterColon(token)

    override fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext> {
        val indentation =
            if (context.newLineAdded) {
                context.indentationManager.getIndentation(context.indentLevel)
            } else {
                ""
            }

        // enforceSingleSpace domina
        if (context.config.enforceSingleSpace == true) {
            return Pair(
                indentation + " : ",
                context.withoutNewLine().copy(colonJustProcessed = false),
            )
        }

        val beforeCfg = context.config.spaceBeforeColon
        val afterCfg = context.config.spaceAfterColon

        val spaceBefore =
            when (beforeCfg) {
                true -> " "
                false -> ""
                null -> "" // No forzar nada
            }

        val spaceAfter =
            when (afterCfg) {
                true -> " "
                false -> ""
                null -> "" // No forzar nada
            }

        // Si ambos son null no añadimos ninguno
        val text =
            if (beforeCfg == null && afterCfg == null) {
                indentation + token.getValue()
            } else {
                indentation + spaceBefore + token.getValue() + spaceAfter
            }

        return Pair(text, context.withoutNewLine().copy(colonJustProcessed = false))
    }
}
