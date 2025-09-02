package node

import Token

class BinaryExpression(
    private val left: Expression,
    private val operator: Token,
    private val right: Expression,
) : Expression {
    fun getLeft(): Expression = left

    fun getOperator(): String = operator.getValue()

    fun getRight(): Expression = right
}
