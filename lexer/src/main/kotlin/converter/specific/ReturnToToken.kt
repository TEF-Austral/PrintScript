package converter.specific

import type.Coordinates
import PrintScriptToken
import Token
import type.CommonTypes

object ReturnToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean = input == "return"

    override fun convert(
        input: String,
        position: Coordinates,
    ): Token = PrintScriptToken(type = CommonTypes.RETURN, value = input, coordinates = position)
}
