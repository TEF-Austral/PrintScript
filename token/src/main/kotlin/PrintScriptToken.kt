class PrintScriptToken(
    private val type: TokenType,
    private val value: String,
    private val coordinates: Coordinates,
) : Token {
    override fun getType(): TokenType = type

    override fun getValue(): String = value

    override fun getCoordinates(): Coordinates = coordinates

    override fun toString(): String = "Token(type=$type, value='$value', coordinates=${coordinates.getRow()}:${coordinates.getColumn()})"
}
