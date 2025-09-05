package node

import Token

class IdentifierExpression(
    private val token: Token,
) : Expression {
    fun getValue(): String = token.getValue()
}
