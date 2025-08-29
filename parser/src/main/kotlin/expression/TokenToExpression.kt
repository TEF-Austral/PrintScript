package parser.expression

import Token
import node.Expression
import parser.Parser

interface TokenToExpression {
    fun build(
        parser: Parser,
        current: Token,
    ): Pair<Expression, Parser>
}
