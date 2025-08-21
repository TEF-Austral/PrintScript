package converter.specific

import Coordinates
import PrintScriptToken
import Token

object AssignmentToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input == "="
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.ASSIGNMENT, value = input, coordinates = position)
    }
}