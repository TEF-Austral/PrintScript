package parser.expression

import TokenType

interface ExpressionBuilder : TokenToExpression {

    fun canHandle(token: TokenType): Boolean

}