package parser.statement

import Token
import parser.Parser
import parser.result.StatementBuiltResult
import parser.result.StatementResult
import parser.utils.isOpeningParenthesis
import parser.utils.isSemiColon
import type.CommonTypes

class ExpressionParser : StatementBuilder {
    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean {
        if (token == null) return false
        return isValidExpressionStart(token.getType(), parser)
    }

    private fun isValidExpressionStart(
        types: CommonTypes,
        parser: Parser,
    ): Boolean =
        when (types) {
            CommonTypes.NUMBER_LITERAL -> true
            CommonTypes.STRING_LITERAL -> true
            CommonTypes.IDENTIFIER -> true
            CommonTypes.OPERATORS -> true
            CommonTypes.DELIMITERS -> isOpeningParenthesis(parser)
            else -> false
        }

    override fun parse(parser: Parser): StatementResult {
        if (isSemiColon(parser.advance().advance().peak())) throw Exception("Invalid structure")
        val expression = parser.getExpressionParser().parseExpression(parser)
        val delimiterParser =
            if (isSemiColon(expression.getParser().peak())) {
                expression.getParser().consume(CommonTypes.DELIMITERS).getParser()
            } else {
                expression.getParser()
            }
        val builtStatement = delimiterParser.getNodeBuilder().buildExpressionStatementNode(expression.getExpression())
        return StatementBuiltResult(delimiterParser, builtStatement)
    }
}
