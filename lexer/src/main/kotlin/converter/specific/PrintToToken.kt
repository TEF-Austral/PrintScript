package converter.specific

import Coordinates
import PrintScriptToken
import Token

object PrintToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input == "println"
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.PRINT, value = input, coordinates = position)
    }
}