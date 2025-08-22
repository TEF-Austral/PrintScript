package parser.expression.primary

import Token
import node.expression.EmptyExpression
import node.expression.Expression
import parser.Parser
import parser.expression.TokenToExpression

class ExpressionRegistry(
    private val expressionBuilder: List<ExpressionBuilder>,
) : TokenToExpression {
    override fun build(
        parser: Parser,
        current: Token,
    ): Pair<Expression, Parser> {
        for (builder in expressionBuilder) {
            if (builder.canHandle(current.getType())) {
                return builder.build(parser, current)
            }
        }
        // default: return both an EmptyExpression and the unmodified parser
        return Pair(EmptyExpression(), parser)
    }
}
