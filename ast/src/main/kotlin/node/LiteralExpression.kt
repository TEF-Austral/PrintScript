package node

import Token
import type.CommonTypes

class LiteralExpression(
    private val token: Token,
) : Expression {
    fun getValue(): String = token.getValue()

    fun getType(): CommonTypes = token.getType()
}
