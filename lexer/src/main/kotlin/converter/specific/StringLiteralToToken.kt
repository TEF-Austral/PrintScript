package converter.specific

import Coordinates
import PrintScriptToken
import Token

object StringLiteralToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input.startsWith("\"") && input.endsWith("\"") ||
                input.startsWith("'") && input.endsWith("'")
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.STRING_LITERAL, value = input, coordinates = position)
    }
}