package converter.specific

import Coordinates
import PrintScriptToken
import Token

object ReturnToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean = input == "return"

    override fun convert(
        input: String,
        position: Coordinates,
    ): Token = PrintScriptToken(type = TokenType.RETURN, value = input, coordinates = position)
}
