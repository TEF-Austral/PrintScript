package converter.specific

import Coordinates
import PrintScriptToken
import Token

object DelimiterToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean = input in listOf("(", ")", "{", "}", ",", ".", ";", ":", "?")

    override fun convert(
        input: String,
        position: Coordinates,
    ): Token = PrintScriptToken(type = TokenType.DELIMITERS, value = input, coordinates = position)
}
