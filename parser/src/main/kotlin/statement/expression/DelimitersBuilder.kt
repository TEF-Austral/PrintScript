package parser.statement.expression

import Token
import node.EmptyExpression
import parser.Parser
import parser.result.ExpressionBuiltResult
import parser.result.ExpressionErrorResult
import parser.result.ExpressionResult
import parser.utils.isClosingParenthesis
import parser.utils.isOpeningParenthesis
import type.CommonTypes

object DelimitersBuilder : ExpressionBuilder {

    override fun canHandle(token: CommonTypes): Boolean = token == CommonTypes.DELIMITERS
    override fun build(
        parser: Parser,
        current: Token,
    ): ExpressionResult {
        if (isOpeningParenthesis(parser)) {
            val afterOpenParen = parser.advance()
            val innerExpression = afterOpenParen.getExpressionParser().parseExpression(afterOpenParen)
            if (!innerExpression.isSuccess()) {
                throw Exception(innerExpression.message())
            }

            if (!isClosingParenthesis(innerExpression.getParser())) {
                throw Exception("Expected closing parenthesis ')'")
            }

            val afterCloseParen = innerExpression.getParser().advance()
            return ExpressionBuiltResult(afterCloseParen, innerExpression.getExpression())
        }
        val newParser = parser.advance()
        val expression = newParser.getExpressionParser().parseExpression(newParser)
        if (expression.getExpression() is EmptyExpression) {
            return ExpressionErrorResult("Surpassed parser length", expression.getParser())
        }
        return ExpressionBuiltResult(expression.getParser().advance(), expression.getExpression())
    }
}
