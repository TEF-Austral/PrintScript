package converter.specific

import type.Coordinates
import PrintScriptToken
import Token
import type.CommonTypes

object DeclarationToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean = input in listOf("let", "const", "var")

    override fun convert(
        input: String,
        position: Coordinates,
    ): Token = PrintScriptToken(type = CommonTypes.DECLARATION, value = input, coordinates = position)
}
