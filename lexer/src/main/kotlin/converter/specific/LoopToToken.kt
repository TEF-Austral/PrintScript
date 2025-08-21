package converter.specific

import Coordinates
import PrintScriptToken
import Token

object LoopToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input in listOf("for", "while", "do")
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.LOOPS, value = input, coordinates = position)
    }
}