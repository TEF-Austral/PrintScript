package converter.specific

import Coordinates
import PrintScriptToken
import Token

object DeclarationToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean = input in listOf("let", "const", "var")

    override fun convert(
        input: String,
        position: Coordinates,
    ): Token = PrintScriptToken(type = TokenType.DECLARATION, value = input, coordinates = position)
}
