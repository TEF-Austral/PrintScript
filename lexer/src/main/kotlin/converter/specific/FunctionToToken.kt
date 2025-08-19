package converter.specific

import Coordinates
import PrintScriptToken
import Token

object FunctionToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input == "function"
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.FUNCTION, value = input, coordinates = position)
    }
}