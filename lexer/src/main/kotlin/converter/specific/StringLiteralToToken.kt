package converter.specific

import coordinates.Coordinates
import PrintScriptToken
import Token
import type.CommonTypes

object StringLiteralToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean =
        input.startsWith("\"") &&
            input.endsWith("\"") ||
            input.startsWith("'") &&
            input.endsWith("'")

    override fun convert(
        input: String,
        position: Coordinates,
    ): Token = PrintScriptToken(type = CommonTypes.STRING_LITERAL, value = input, coordinates = position)
}
