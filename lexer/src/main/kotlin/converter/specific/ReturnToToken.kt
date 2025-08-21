package converter.specific

import Coordinates
import PrintScriptToken
import Token

object ReturnToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input == "return"
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.RETURN, value = input, coordinates = position)
    }
}