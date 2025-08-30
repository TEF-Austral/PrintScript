package parser.expression.primary

import Token
import node.Expression
import parser.Parser
import type.CommonTypes

object DelimitersBuilder : ExpressionBuilder {
    override fun build(
        parser: Parser,
        current: Token,
    ): Pair<Expression, Parser> {
        // skip '('
        val afterOpen = parser.advance()
        // parse inner expression
        val (expr, afterExpr) = afterOpen.getExpressionParser().parseExpression(afterOpen)
        // skip ')'
        val afterClose = afterExpr.advance()
        return Pair(expr, afterClose)
    }

    override fun canHandle(token: CommonTypes): Boolean = token == CommonTypes.DELIMITERS
}
