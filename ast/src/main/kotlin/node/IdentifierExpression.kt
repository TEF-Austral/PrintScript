package node

import Token

class IdentifierExpression(
    private val token: Token,
) : Expression {
    fun getValue(): String = token.getValue()

    fun getToken(): Token = token // Public getter for the token
}
