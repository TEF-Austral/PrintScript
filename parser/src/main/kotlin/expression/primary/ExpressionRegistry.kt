package parser.expression.primary

import Token
import node.EmptyExpression
import parser.Parser
import parser.expression.TokenToExpression
import parser.result.ExpressionBuiltResult

class ExpressionRegistry(
    private val expressionBuilder: List<ExpressionBuilder>,
) : TokenToExpression {
    override fun build(
        parser: Parser,
        current: Token,
    ): ExpressionBuiltResult {
        for (builder in expressionBuilder) {
            if (builder.canHandle(current.getType())) {
                return builder.build(parser, current)
            }
        }
        return ExpressionBuiltResult(parser, EmptyExpression())
    }
}
