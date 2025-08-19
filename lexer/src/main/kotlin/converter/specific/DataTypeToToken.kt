package converter.specific

import Coordinates
import PrintScriptToken
import Token

object DataTypeToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input in listOf("number", "string")
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.DATA_TYPES, value = input, coordinates = position)
    }
}