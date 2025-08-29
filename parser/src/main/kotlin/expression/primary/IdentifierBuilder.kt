package parser.expression.primary

import Token
import TokenType
import node.Expression
import parser.Parser

object IdentifierBuilder : ExpressionBuilder {
    override fun canHandle(token: TokenType): Boolean = token == TokenType.IDENTIFIER

    override fun build(
        parser: Parser,
        current: Token,
    ): Pair<Expression, Parser> {
        val nextParser = parser.advance()
        val expr = nextParser.getNodeBuilder().buildIdentifierNode(current)
        return Pair(expr, nextParser)
    }
}
