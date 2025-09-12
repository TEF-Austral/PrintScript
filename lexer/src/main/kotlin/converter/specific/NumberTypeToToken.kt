package converter.specific

import coordinates.Coordinates
import PrintScriptToken
import Token
import type.CommonTypes

object NumberTypeToToken : StringToTokenConverter {
    private val acceptedNumberTypes = setOf("Number", "number")

    override fun canHandle(input: String): Boolean = input in acceptedNumberTypes

    override fun convert(
        input: String,
        position: Coordinates,
    ): Token =
        PrintScriptToken(
            type = CommonTypes.NUMBER,
            value = input,
            coordinates = position,
        )
}
