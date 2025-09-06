package node

import Token

class PrintStatement(
    private val expression: Expression,
) : Statement {
    fun getExpression(): Expression = expression

    fun getExpressionToken(): Token? =
        if (expression is IdentifierExpression) {
            expression.getToken()
        } else {
            null
        }
}
