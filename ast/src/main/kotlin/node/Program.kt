package node

import coordinates.Coordinates
import coordinates.Position

class Program(
    private val statements: List<Statement>,
    private val coordinates: Coordinates = Position(0, 0),
) : ASTNode {
    fun getStatements(): List<Statement> = statements

    override fun getCoordinates(): Coordinates = coordinates
}
