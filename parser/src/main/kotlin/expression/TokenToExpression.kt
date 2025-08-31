package parser.expression

import Token
import parser.Parser
import parser.result.ExpressionBuiltResult

interface TokenToExpression {
    fun build(
        parser: Parser,
        current: Token,
    ): ExpressionBuiltResult
}
