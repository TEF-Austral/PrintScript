package converter.specific

import coordinates.Coordinates
import PrintScriptToken
import Token
import type.CommonTypes

object LoopToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean = input in listOf("for", "while", "do")

    override fun convert(
        input: String,
        position: Coordinates,
    ): Token = PrintScriptToken(type = CommonTypes.LOOPS, value = input, coordinates = position)
}
