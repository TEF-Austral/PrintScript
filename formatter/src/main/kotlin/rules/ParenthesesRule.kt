import formatter.engine.FormatterContext
import formatter.rules.FormattingRule
import type.CommonTypes

class ParenthesesRule : FormattingRule {
    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean {
        val value = token.getValue().trim()
        return value == "(" || value == ")"
    }

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

        val value = token.getValue().trim()

        val text =
            when (value) {
                "(" -> {
                    val space =
                        when {
                            context.newLineAdded -> ""
                            context.previousToken?.getType() == CommonTypes.IDENTIFIER -> ""
                            context.config.enforceSingleSpace == true -> " "
                            else ->
                                context.spaceManager.getSpaceBetween(
                                    context.previousToken,
                                    token,
                                    null,
                                )
                        }
                    indentation + space + value
                }
                ")" -> {
                    val space =
                        when {
                            context.newLineAdded -> ""
                            context.config.enforceSingleSpace == true -> " "
                            else -> ""
                        }
                    indentation + space + value
                }
                else -> indentation + value
            }

        return Pair(text, context.withoutNewLine())
    }
}
