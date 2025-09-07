package node

import coordinates.Coordinates
import coordinates.Position

class EmptyStatement(
    private val coordinates: Coordinates = Position(0, 0),
) : Statement {
    override fun toString(): String = "EmptyStatement"

    override fun getCoordinates(): Coordinates = coordinates
}
