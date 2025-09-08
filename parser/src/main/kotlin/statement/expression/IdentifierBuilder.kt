package parser.statement.expression

import Token
import type.CommonTypes
import parser.Parser
import parser.result.ExpressionBuiltResult

class IdentifierBuilder : ExpressionBuilder {
    override fun canHandle(token: CommonTypes): Boolean = token == CommonTypes.IDENTIFIER

    override fun build(
        parser: Parser,
        current: Token,
    ): ExpressionBuiltResult {
        val newParser = parser.advance()
        return ExpressionBuiltResult(
            newParser,
            newParser.getNodeBuilder().buildIdentifierNode(current),
        )
    }
}
