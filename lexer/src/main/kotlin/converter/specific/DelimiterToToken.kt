package converter.specific

import type.Coordinates
import PrintScriptToken
import Token
import type.CommonTypes

object DelimiterToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean = input in listOf("(", ")", "{", "}", ",", ".", ";", ":", "?")

    override fun convert(
        input: String,
        position: Coordinates,
    ): Token = PrintScriptToken(type = CommonTypes.DELIMITERS, value = input, coordinates = position)
}
