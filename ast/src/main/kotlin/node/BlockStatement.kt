package node

import coordinates.Coordinates

class BlockStatement(
    private val statements: List<Statement>,
    private val coordinates: Coordinates
) : Statement {
    fun getStatements(): List<Statement> = statements

    override fun getCoordinates(): Coordinates = coordinates
}
