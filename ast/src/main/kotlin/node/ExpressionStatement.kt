package node

import coordinates.Coordinates
import coordinates.Position

class ExpressionStatement(
    private val expression: Expression,
    private val coordinates: Coordinates,
) : Statement {
    fun getExpression(): Expression = expression

    override fun getCoordinates(): Coordinates = coordinates
}
