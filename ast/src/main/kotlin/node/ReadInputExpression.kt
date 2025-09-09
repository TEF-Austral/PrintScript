package node

import coordinates.Coordinates

class ReadInputExpression(
    private val printValue: String,
    private val coordinates: Coordinates,
) : CoercibleExpression {
    override fun getCoordinates(): Coordinates = coordinates

    fun printValue(): String = printValue
}
