package parser.statement.expression

import Token
import parser.Parser
import parser.result.ExpressionResult

sealed interface TokenToExpression {
    fun build(
        parser: Parser,
        current: Token,
    ): ExpressionResult
}
