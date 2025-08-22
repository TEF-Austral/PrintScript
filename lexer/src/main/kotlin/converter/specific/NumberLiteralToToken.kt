package converter.specific

import Coordinates
import PrintScriptToken
import Token

object NumberLiteralToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean = input.toDoubleOrNull() != null

    override fun convert(
        input: String,
        position: Coordinates,
    ): Token = PrintScriptToken(type = TokenType.NUMBER_LITERAL, value = input, coordinates = position)
}
