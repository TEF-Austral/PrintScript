package parser.expression

import Token
import type.CommonTypes
import parser.Parser
import parser.result.ExpressionBuiltResult

object LiteralBuilder : ExpressionBuilder {
    override fun canHandle(token: CommonTypes): Boolean = token == CommonTypes.NUMBER_LITERAL || token == CommonTypes.STRING_LITERAL

    override fun build(
        parser: Parser,
        current: Token,
    ): ExpressionBuiltResult {
        val newParser = parser.advance()
        return ExpressionBuiltResult(
            newParser,
            newParser.getNodeBuilder().buildLiteralExpressionNode(current),
        )
    }
}
