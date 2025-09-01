package parser.expression

import Token
import parser.Parser
import parser.result.ExpressionResult

interface TokenToExpression {
    fun build(
        parser: Parser,
        current: Token,
    ): ExpressionResult
}
