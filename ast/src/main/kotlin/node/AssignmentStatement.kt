package node

import Token
import coordinates.Coordinates
import coordinates.Position

class AssignmentStatement(
    private val identifier: Token,
    private val value: Expression,
    private val coordinates: Coordinates = Position(0, 0),
) : Statement {
    fun getIdentifier(): String = identifier.getValue()

    fun getValue(): Expression = value

    fun getIdentifierToken(): Token = identifier

    override fun getCoordinates(): Coordinates = coordinates
}
