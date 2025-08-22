package converter.specific

import Coordinates
import PrintScriptToken
import Token

object LogicalOperatorToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean = input in listOf("&&", "||")

    override fun convert(
        input: String,
        position: Coordinates,
    ): Token = PrintScriptToken(type = TokenType.LOGICAL_OPERATORS, value = input, coordinates = position)
}
