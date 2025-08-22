package converter.specific

import Coordinates
import PrintScriptToken
import Token

object FunctionToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean = input == "function"

    override fun convert(
        input: String,
        position: Coordinates,
    ): Token = PrintScriptToken(type = TokenType.FUNCTION, value = input, coordinates = position)
}
