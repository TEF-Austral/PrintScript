package converter.specific

import coordinates.Coordinates
import PrintScriptToken
import Token
import type.CommonTypes

object DataTypeToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean = input in listOf("Number", "String")

    override fun convert(
        input: String,
        position: Coordinates,
    ): Token {
        val dataType = getDataType(input)
        return PrintScriptToken(dataType, value = input, coordinates = position)
    }

    private fun getDataType(input: String): CommonTypes =
        when (input){
            "String" -> CommonTypes.STRING
            "Number" -> CommonTypes.NUMBER
            else -> CommonTypes.DATA_TYPES
        }
}
