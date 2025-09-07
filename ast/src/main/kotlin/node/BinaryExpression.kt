package node

import Token
import coordinates.Coordinates
import coordinates.Position

class BinaryExpression(
    private val left: Expression,
    private val operator: Token,
    private val right: Expression,
    private val coordinates: Coordinates = Position(0, 0),
) : Expression {
    fun getLeft(): Expression = left

    fun getOperator(): String = operator.getValue()

    fun getRight(): Expression = right

    override fun getCoordinates(): Coordinates = coordinates
}
