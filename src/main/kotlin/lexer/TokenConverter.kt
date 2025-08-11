package lexer

import java.math.BigInteger
import token.Coordinates
import token.PrintScriptToken
import token.Token
import token.TokenType

interface TokenConverter {

    fun convert(input: String,position: Coordinates): Token
}

class StringToPrintScriptToken(private val map: Map<String, TokenType>) : TokenConverter {

    override fun convert(input: String, position: Coordinates): Token {
       val type = map[input] ?: getTokenType(input)
        return PrintScriptToken(type, input, position)
    }


    // TODO habria que cambiar como se lee esto pd: no se si funciona, deberia de
    private fun getTokenType(lexeme: String): TokenType {
        return map[lexeme] ?: when {
            lexeme.matches(Regex("^[0-9]+(\\.[0-9]+)?$")) -> {
                if (lexeme.contains('.')) {
                    TokenType.NUMBER_LITERAL  // reemplaza FLOAT_LITERAL
                } else {
                    val value = lexeme.toBigIntegerOrNull()
                    if (value != null && value > BigInteger.valueOf(Long.MAX_VALUE)) {
                        TokenType.BIGINT_LITERAL
                    } else {
                        TokenType.NUMBER_LITERAL // o NUMBER_LITERAL si prefieres
                    }
                }
            }
            (lexeme.startsWith("\"") && lexeme.endsWith("\"")) ||
                    (lexeme.startsWith("'") && lexeme.endsWith("'")) -> TokenType.STRING_LITERAL
            else -> TokenType.IDENTIFIER
        }
    }


}