package parser.expression.primary

import Token
import TokenType
import node.expression.Expression
import parser.Parser

object DelimitersBuilder : ExpressionBuilder {
    override fun canHandle(token: TokenType): Boolean = token == TokenType.DELIMITERS

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
}
