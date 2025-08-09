package token

sealed interface Token {
    fun getType(): TokenType
    fun getValue(): String
    fun getCoordinates(): Coordinates
}

class PrintScriptToken(private val type: TokenType,
                       private val value: String,
                       private val coordinates: Coordinates): Token {

    override fun getType(): TokenType {
        return type
    }

    override fun getValue(): String {
        return value
    }

    override fun getCoordinates(): Coordinates {
        return coordinates
    }
}