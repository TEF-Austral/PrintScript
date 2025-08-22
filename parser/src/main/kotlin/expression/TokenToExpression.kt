package parser.expression

import Token
import node.expression.Expression
import parser.Parser

interface TokenToExpression {
    fun build(
        parser: Parser,
        current: Token,
    ): Expression
}
