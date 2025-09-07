package parser.statement.expression

import Token
import type.CommonTypes
import parser.Parser
import parser.result.ExpressionBuiltResult

class LiteralBuilder(
    private val handleableLiteralTypes: List<CommonTypes> =
        listOf(CommonTypes.NUMBER_LITERAL, CommonTypes.STRING_LITERAL),
) : ExpressionBuilder {
    override fun canHandle(token: CommonTypes): Boolean {
        for (type in handleableLiteralTypes) {
            if (token == type) return true
        }
        return false
    }

    override fun build(
        parser: Parser,
        current: Token,
    ): ExpressionBuiltResult {
        val newParser = parser.advance()
        return ExpressionBuiltResult(
            newParser,
            newParser.getNodeBuilder().buildLiteralExpressionNode(current, current.getCoordinates()),
        )
    }
}
