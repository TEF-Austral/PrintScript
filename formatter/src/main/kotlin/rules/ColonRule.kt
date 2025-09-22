package formatter.rules

import Token
import formatter.engine.FormatterContext
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
        val indentation = getIndentation(context)
        val text = formatColonText(token, context, indentation)
        val updatedContext = context.withoutNewLine().copy(colonJustProcessed = false)
        return Pair(text, updatedContext)
    }

    private fun getIndentation(context: FormatterContext): String =
        if (context.newLineAdded) {
            context.indentationManager.getIndentation(context.indentLevel)
        } else {
            ""
        }

    private fun formatColonText(
        token: Token,
        context: FormatterContext,
        indentation: String,
    ): String = formatWithSpaces(token, context, indentation)

    private fun formatWithSpaces(
        token: Token,
        context: FormatterContext,
        indentation: String,
    ): String {
        val beforeCfg = context.config.spaceBeforeColon
        val afterCfg = context.config.spaceAfterColon
        val spaceBefore = getSpace(beforeCfg)
        val spaceAfter = getSpace(afterCfg)
        return if (beforeCfg == null && afterCfg == null) {
            indentation + token.getValue()
        } else {
            indentation + spaceBefore + token.getValue() + spaceAfter
        }
    }

    private fun getSpace(cfg: Boolean?): String = if (cfg == true) " " else ""
}
