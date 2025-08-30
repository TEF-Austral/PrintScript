package parser.expression.primary

import Token
import node.Expression
import parser.Parser
import type.CommonTypes

object IdentifierBuilder : ExpressionBuilder {
    override fun canHandle(token: CommonTypes): Boolean = token == CommonTypes.IDENTIFIER

    override fun build(
        parser: Parser,
        current: Token,
    ): Pair<Expression, Parser> {
        val nextParser = parser.advance()
        val expr = nextParser.getNodeBuilder().buildIdentifierNode(current)
        return Pair(expr, nextParser)
    }
}
