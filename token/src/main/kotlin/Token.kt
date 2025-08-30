import type.CommonTypes
import type.Coordinates

sealed interface Token {
    fun getType(): CommonTypes

    fun getValue(): String

    fun getCoordinates(): Coordinates
}
