package node

import coordinates.Coordinates
import coordinates.Position

class EmptyExpression(private val coordinates: Coordinates = Position(0, 0)): Expression {
    override fun toString(): String = "EmptyExpression"
    override fun getCoordinates(): Coordinates = coordinates
}
