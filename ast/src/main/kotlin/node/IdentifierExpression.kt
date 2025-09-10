package node

import Token
import coordinates.Coordinates

class IdentifierExpression(
    private val token: Token,
    private val coordinates: Coordinates,
) : Expression {
    fun getValue(): String = token.getValue()

    fun getToken(): Token = token // Public getter for the token

    override fun getCoordinates(): Coordinates = coordinates
}
