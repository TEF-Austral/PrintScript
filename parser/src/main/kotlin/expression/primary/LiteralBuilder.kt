package parser.expression.primary

import Token
import TokenType
import node.expression.Expression
import parser.Parser

object LiteralBuilder : ExpressionBuilder {
    override fun canHandle(token: TokenType): Boolean = token == TokenType.NUMBER_LITERAL || token == TokenType.STRING_LITERAL

    override fun build(
        parser: Parser,
        current: Token,
    ): Pair<Expression, Parser> {
        val nextParser = parser.advance()
        val expr = nextParser.getNodeBuilder().buildLiteralExpressionNode(current)
        return Pair(expr, nextParser)
    }
}
