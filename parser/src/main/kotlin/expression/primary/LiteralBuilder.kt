package parser.expression.primary

import Token
import TokenType
import node.expression.Expression
import parser.Parser
import parser.expression.primary.ExpressionBuilder

object LiteralBuilder : ExpressionBuilder {

    override fun canHandle(token: TokenType): Boolean {
        return token == TokenType.NUMBER_LITERAL || token == TokenType.STRING_LITERAL
    }

    override fun build(parser: Parser, current: Token): Expression {
        parser.advance()
        return parser.getNodeBuilder().buildLiteralExpressionNode(current)
    }
}