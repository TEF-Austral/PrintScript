package lexer

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

    private fun getTokenType(lexeme: String): TokenType {
        return map[lexeme]
            ?: when {
                lexeme.matches(Regex("^[0-9]+(\\.[0-9]+)?$")) ->
                    if (lexeme.contains('.')) TokenType.FLOAT_LITERAL else TokenType.INTEGER_LITERAL
                (lexeme.startsWith("\"") && lexeme.endsWith("\"")) ||
                        (lexeme.startsWith("'") && lexeme.endsWith("'")) ->
                    TokenType.STRING_LITERAL
                else ->
                    TokenType.IDENTIFIER
            }
    }

}