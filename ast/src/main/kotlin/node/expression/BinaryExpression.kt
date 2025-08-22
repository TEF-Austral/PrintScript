package node.expression

import Token

class BinaryExpression(
    private val left: Expression,
    private val operator: Token,
    private val right: Expression,
) : Expression {
    fun getLeft(): Expression = left

    fun getOperator(): Token = operator

    fun getRight(): Expression = right
}
