package node

import Token
import coordinates.Coordinates

class BinaryExpression(
    private val left: Expression,
    private val operator: Token,
    private val right: Expression,
    private val coordinates: Coordinates,
) : Expression {
    fun getLeft(): Expression = left

    fun getOperator(): String = operator.getValue()

    fun getRight(): Expression = right

    override fun getCoordinates(): Coordinates = coordinates
}
