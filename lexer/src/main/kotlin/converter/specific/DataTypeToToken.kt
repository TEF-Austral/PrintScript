package converter.specific

import type.Coordinates
import PrintScriptToken
import Token
import type.CommonTypes

object DataTypeToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean = input in listOf("Number", "String")

    override fun convert(
        input: String,
        position: Coordinates,
    ): Token = PrintScriptToken(type = CommonTypes.DATA_TYPES, value = input, coordinates = position)
}
