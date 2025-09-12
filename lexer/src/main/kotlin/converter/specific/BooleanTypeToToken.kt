package converter.specific

import coordinates.Coordinates
import PrintScriptToken
import Token
import type.CommonTypes

object BooleanTypeToToken : StringToTokenConverter {
    private val acceptedBooleanTypes = setOf("Boolean", "boolean")

    override fun canHandle(input: String): Boolean = input in acceptedBooleanTypes

    override fun convert(
        input: String,
        position: Coordinates,
    ): Token =
        PrintScriptToken(
            type = CommonTypes.BOOLEAN,
            value = input,
            coordinates = position,
        )
}
