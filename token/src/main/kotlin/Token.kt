sealed interface Token {
    fun getType(): TokenType

    fun getValue(): String

    fun getCoordinates(): Coordinates
}
