package parser.expression.primary

import Token
import parser.Parser
import parser.result.ExpressionBuiltResult
import type.CommonTypes

object DelimitersBuilder : ExpressionBuilder {
    override fun canHandle(token: CommonTypes): Boolean = token == CommonTypes.DELIMITERS

    override fun build(
        parser: Parser,
        current: Token,
    ): ExpressionBuiltResult {
        val newParser = parser.advance()
        val expression = newParser.getExpressionParser().parseExpression(newParser)
        return ExpressionBuiltResult(expression.getParser().advance(), expression.getExpression())
    }
}
