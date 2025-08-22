package parser.expression

import Token
import node.expression.Expression
import node.statement.Statement
import parser.Parser
import parser.result.ParseResult

interface TokenToExpression {
    fun build(
        parser: Parser,
        current: Token,
    ): Pair<Expression, Parser>
}
