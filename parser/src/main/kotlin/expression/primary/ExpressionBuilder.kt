package parser.expression.primary

import TokenType
import parser.expression.TokenToExpression

interface ExpressionBuilder : TokenToExpression {
    fun canHandle(token: TokenType): Boolean
}
