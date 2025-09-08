package node

import coordinates.Coordinates

class ReadInputStatement(
    private val printValue: String,
    private val coordinates: Coordinates,
) : Statement {
    override fun getCoordinates(): Coordinates = coordinates

    fun printValue(): String = printValue
}
