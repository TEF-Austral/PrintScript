package converter.specific

import Coordinates
import PrintScriptToken
import Token

object ConditionalToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input in listOf("if", "else", "switch", "case")
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.CONDITIONALS, value = input, coordinates = position)
    }
}