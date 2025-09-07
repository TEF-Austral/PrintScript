package node

import Token
import coordinates.Coordinates
import coordinates.Position
import type.CommonTypes

class LiteralExpression(
    private val token: Token,
    private val coordinates: Coordinates = Position(0, 0),
) : Expression {
    fun getValue(): String = token.getValue()

    fun getType(): CommonTypes = token.getType()

    override fun getCoordinates(): Coordinates = coordinates
}
