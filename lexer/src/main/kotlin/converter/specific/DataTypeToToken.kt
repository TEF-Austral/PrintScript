package converter.specific

import Coordinates
import PrintScriptToken
import Token

object DataTypeToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean = input in listOf("Number", "String")

    override fun convert(
        input: String,
        position: Coordinates,
    ): Token = PrintScriptToken(type = TokenType.DATA_TYPES, value = input, coordinates = position)
}
