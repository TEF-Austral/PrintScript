package parser.expression.primary

import Token
import node.Expression
import parser.Parser
import type.CommonTypes

object LiteralBuilder : ExpressionBuilder {
    override fun canHandle(token: CommonTypes): Boolean = token == CommonTypes.NUMBER_LITERAL || token == CommonTypes.STRING_LITERAL

    override fun build(
        parser: Parser,
        current: Token,
    ): Pair<Expression, Parser> {
        val nextParser = parser.advance()
        val expr = nextParser.getNodeBuilder().buildLiteralExpressionNode(current)
        return Pair(expr, nextParser)
    }
}
