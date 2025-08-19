package converter.specific

import Coordinates
import PrintScriptToken
import Token

object NumberLiteralToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input.toDoubleOrNull() != null
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.NUMBER_LITERAL, value = input, coordinates = position)
    }
}