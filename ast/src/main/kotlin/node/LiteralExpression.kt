package node

import Token

class LiteralExpression(
    private val token: Token,
) : Expression {
    fun getValue(): String = token.getValue()
}
