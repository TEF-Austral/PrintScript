package parser.expression.primary

import Token
import TokenType
import node.expression.Expression
import parser.Parser

object IdentifierBuilder : ExpressionBuilder {

    override fun canHandle(token: TokenType): Boolean {
        return token == TokenType.IDENTIFIER
    }

    override fun build(parser: Parser, current: Token): Expression {
        parser.advance()
        return parser.getNodeBuilder().buildIdentifierNode(current)
    }

}