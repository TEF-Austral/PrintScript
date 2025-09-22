package formatter.rules

import Token
import formatter.engine.FormatterContext
import formatter.util.isAssignment
import formatter.util.isDelimiterColon
import type.CommonTypes

object SingleSpaceEnforcer {
    fun enforce(
        token: Token,
        context: FormatterContext,
        formattedText: String,
    ): String {
        if (!shouldEnforce(context)) return formattedText
        return when {
            isAssignment(token) -> enforceAssignment(formattedText)
            isDelimiterColon(token) -> enforceColon(formattedText)
            isOpenParen(token) -> enforceOpenParen(formattedText, context)
            isCloseParen(token) -> enforceCloseParen(formattedText)
            else -> formattedText
        }
    }

    private fun shouldEnforce(context: FormatterContext) =
        context.config.enforceSingleSpace == true && !context.newLineAdded

    private fun enforceAssignment(text: String): String = text.replace(Regex("\\s*=\\s*"), " = ")

    private fun enforceColon(text: String): String = text.replace(Regex("\\s*:\\s*"), " : ")

    private fun isOpenParen(token: Token) = token.getValue().trim() == "("

    private fun isCloseParen(token: Token) = token.getValue().trim() == ")"

    private fun enforceOpenParen(
        text: String,
        context: FormatterContext,
    ): String {
        val noSpace = text.replace(Regex("^\\s*\\("), "(")
        return if (context.previousToken?.getType() == CommonTypes.IDENTIFIER) {
            noSpace
        } else {
            text.replace(Regex("^\\s*\\("), " (")
        }
    }

    private fun enforceCloseParen(text: String): String = text.replace(Regex("^\\s*\\)"), " )")
}
