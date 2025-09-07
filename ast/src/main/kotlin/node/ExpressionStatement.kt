package node

import coordinates.Coordinates
import coordinates.Position

class ExpressionStatement(
    private val expression: Expression,
    private val coordinates: Coordinates = Position(0, 0),
) : Statement {
    fun getExpression(): Expression = expression

    override fun getCoordinates(): Coordinates = coordinates
}
