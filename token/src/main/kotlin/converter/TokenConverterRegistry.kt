package converter

import Coordinates
import PrintScriptToken
import Token
import TokenType

data class TokenConverterRegistry(val list: List<StringToTokenConverter>) : TokenConverter {

    override fun convert(input: String, position: Coordinates): Token {
        for (converter in list) {
            if (converter.canHandle(input)) {
                return converter.convert(input, position)
            }
        }
        return PrintScriptToken(type = TokenType.IDENTIFIER, value = input, coordinates = position)
    }
}