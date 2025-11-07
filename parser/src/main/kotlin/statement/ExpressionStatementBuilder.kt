package parser.statement

import Token
import parser.Parser
import parser.exception.ParserException
import parser.result.StatementBuiltResult
import parser.result.StatementResult
import parser.utils.advancePastSemiColon
import parser.utils.isOpeningParenthesis
import parser.utils.isSemiColon
import type.CommonTypes

class ExpressionStatementBuilder(
    private val acceptedTypes: Map<CommonTypes, Boolean> =
        mapOf(
            CommonTypes.NUMBER_LITERAL to true,
            CommonTypes.STRING_LITERAL to true,
            CommonTypes.IDENTIFIER to true,
            CommonTypes.OPERATORS to true,
        ),
) : StatementBuilder {
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
    ): Boolean {
        if (acceptedTypes.containsKey(types)) {
            return acceptedTypes.getValue(types)
        } else if (types == CommonTypes.DELIMITERS) {
            return isOpeningParenthesis(parser)
        }
        return false
    }

    override fun parse(parser: Parser): StatementResult {
        val possibleSemiColon = parser.advance().advance().peak()
        if (isSemiColon(possibleSemiColon)) {
            val coordinates = possibleSemiColon!!.getCoordinates()
            throw ParserException("Invalid expression structure", coordinates)
        }
        val expression = parser.getExpressionParser().parseExpression(parser)
        val currentToken = expression.getParser().peak()
        if (!isSemiColon(currentToken)) {
            throw ParserException(
                "Expected ';' at the end of expression statement. But found ${currentToken!!.getValue()}",
                currentToken.getCoordinates(),
            )
        }
        val delimiterParser = advancePastSemiColon(expression.getParser())
        val builtStatement =
            delimiterParser.getNodeBuilder().buildExpressionStatementNode(
                expression.getExpression(),
            )
        return StatementBuiltResult(delimiterParser, builtStatement)
    }
}
