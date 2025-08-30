package converter.specific

import type.Coordinates
import PrintScriptToken
import Token
import type.CommonTypes

object FunctionToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean = input == "function"

    override fun convert(
        input: String,
        position: Coordinates,
    ): Token = PrintScriptToken(type = CommonTypes.FUNCTION, value = input, coordinates = position)
}
