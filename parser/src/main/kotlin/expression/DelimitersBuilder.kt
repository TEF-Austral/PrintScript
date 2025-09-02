package parser.expression

import Token
import node.EmptyExpression
import parser.Parser
import parser.result.ExpressionBuiltResult
import parser.result.ExpressionErrorResult
import parser.result.ExpressionResult
import type.CommonTypes

object DelimitersBuilder : ExpressionBuilder {
    override fun canHandle(token: CommonTypes): Boolean = token == CommonTypes.DELIMITERS

    override fun build(
        parser: Parser,
        current: Token,
    ): ExpressionResult {
        val newParser = parser.advance()
        val expression = newParser.getExpressionParser().parseExpression(newParser)
        if (expression.getExpression() is EmptyExpression) {
            return ExpressionErrorResult("Surpassed parser length", expression.getParser())
        }
        return ExpressionBuiltResult(expression.getParser().advance(), expression.getExpression())
    }
}
