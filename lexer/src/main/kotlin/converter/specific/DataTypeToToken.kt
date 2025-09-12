package converter.specific

import coordinates.Coordinates
import PrintScriptToken
import Token
import type.CommonTypes

class DataTypeToToken(
    private val acceptedDataTypes: Map<String, CommonTypes> =
        mapOf(
            "Number" to CommonTypes.NUMBER,
            "number" to CommonTypes.NUMBER,
            "String" to CommonTypes.STRING,
            "string" to CommonTypes.STRING,
        ),
) : StringToTokenConverter {

    override fun canHandle(input: String): Boolean = input in acceptedDataTypes

    override fun convert(
        input: String,
        position: Coordinates,
    ): Token {
        val dataType = getDataType(input)
        return PrintScriptToken(dataType, value = input, coordinates = position)
    }

    private fun getDataType(input: String): CommonTypes = acceptedDataTypes.getValue(input)
}
