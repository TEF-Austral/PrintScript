package node

import Token

class IdentifierExpression(
    private val token: Token,
) : Expression {
    fun getName(): String = token.getValue()
}
