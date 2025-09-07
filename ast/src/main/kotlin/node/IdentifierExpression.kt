package node

import Token
import coordinates.Coordinates
import coordinates.Position

class IdentifierExpression(
    private val token: Token,
    private val coordinates: Coordinates = Position(0, 0),
) : Expression {
    fun getValue(): String = token.getValue()

    fun getToken(): Token = token // Public getter for the token

    override fun getCoordinates(): Coordinates = coordinates
}
