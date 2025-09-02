package parser.command

import Token
import parser.Parser
import parser.result.StatementBuiltResult
import parser.result.StatementResult
import type.CommonTypes

class ExpressionParser : StatementCommand {
    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean {
        if (token == null) return false

        val commonTypes = token.getType()
        val tokenValue = token.getValue()

        return isNumberLiteral(commonTypes) ||
            isStringLiteral(commonTypes) ||
            isIdentifier(commonTypes) ||
            isOpeningParenthesis(commonTypes, tokenValue)
    }

    private fun isNumberLiteral(commonTypes: CommonTypes): Boolean = commonTypes == CommonTypes.NUMBER_LITERAL

    private fun isStringLiteral(commonTypes: CommonTypes): Boolean = commonTypes == CommonTypes.STRING_LITERAL

    private fun isIdentifier(commonTypes: CommonTypes): Boolean = commonTypes == CommonTypes.IDENTIFIER

    private fun isOpeningParenthesis(
        commonTypes: CommonTypes,
        tokenValue: String,
    ): Boolean = commonTypes == CommonTypes.DELIMITERS && tokenValue == "("

    override fun parse(parser: Parser): StatementResult {
        val expression = parser.getExpressionParser().parseExpression(parser)
        val delimiterParser = expression.getParser().consume(CommonTypes.DELIMITERS).getParser() // ;
        // TODO ACA HAY PROBLEMAS
        val builtStatement = delimiterParser.getNodeBuilder().buildExpressionStatementNode(expression.getExpression())
        return StatementBuiltResult(delimiterParser, builtStatement)
    }
}
