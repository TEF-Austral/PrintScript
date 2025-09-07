package node

import coordinates.Coordinates

sealed interface ASTNode{
    fun getCoordinates(): Coordinates
}