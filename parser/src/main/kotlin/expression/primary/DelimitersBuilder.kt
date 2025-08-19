package parser.expression.primary

import Token
import TokenType
import node.expression.Expression
import parser.Parser
import parser.expression.ExpressionBuilder

object DelimitersBuilder : ExpressionBuilder {

    override fun canHandle(token: TokenType): Boolean {
        return token == TokenType.DELIMITERS
    }

    override fun build(parser: Parser, current: Token): Expression {
        parser.advance() // asumimos que es "("
        val expression = parser.getExpressionParser().parseExpression(parser)
        parser.advance() // avanzamos al siguiente token, que debería ser ")"
        return expression
    }


}