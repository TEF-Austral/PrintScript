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

        // Handle enforceSingleSpace - it overrides other colon spacing configs
        val (spaceBefore, spaceAfter) =
            if (context.config.enforceSingleSpace == true) {
                // When enforceSingleSpace is true, add space around colons
                " " to " "
            } else {
                // Otherwise use specific colon configuration
                val before =
                    when (context.config.spaceBeforeColon) {
                        true -> " "
                        false -> ""
                        null -> "" // Default: NO space before colon
                    }
                val after =
                    when (context.config.spaceAfterColon) {
                        true -> " "
                        false -> ""
                        null -> "" // Default: NO space after colon
                    }
                before to after
            }

        val formattedText = indentation + spaceBefore + ":" + spaceAfter

        // Set flag to indicate colon was just processed
        val newContext = context.withoutNewLine().copy(colonJustProcessed = true)
        return Pair(formattedText, newContext)
    }
}
