package node

import coordinates.Coordinates

class ReadEnvStatement(private val envName: String,
                       private val coordinates: Coordinates): Statement {
    override fun getCoordinates(): Coordinates = coordinates
    fun envName(): String = envName
}