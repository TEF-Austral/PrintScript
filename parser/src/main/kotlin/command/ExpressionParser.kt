package parser.command

import Token
import parser.Parser
import parser.result.StatementBuiltResult
import parser.result.StatementResult
import parser.utils.isSemiColon
import type.CommonTypes

class ExpressionParser : StatementCommand {
    override fun canHandle(token: Token?, parser: Parser, ): Boolean {
        if (token == null) return false
        val types = token.getType()
        return isNumberLiteral(types) || isStringLiteral(types) || isIdentifier(types) || isOperator(types)
    }

    private fun isNumberLiteral(types: CommonTypes): Boolean = types == CommonTypes.NUMBER_LITERAL

    private fun isStringLiteral(types: CommonTypes): Boolean = types == CommonTypes.STRING_LITERAL

    private fun isIdentifier(types: CommonTypes): Boolean = types == CommonTypes.IDENTIFIER

    private fun isOperator(types: CommonTypes): Boolean = types == CommonTypes.OPERATORS

    override fun parse(parser: Parser): StatementResult {
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
