package lexer

import lexer.reader.Reader
import token.Token
import token.TokenType

sealed interface Lexer {
    fun tokenize(typeMap: Map<String, TokenType>): List<Token>
}
class PrintScriptLexer(val reader: Reader, val splitter: Splitter): Lexer {

    override fun tokenize(typeMap: Map<String, TokenType>): List<Token> {
        val stringWithCoordinates = splitter.split(reader.read())
        val converter: TokenConverter = StringToPrintScriptToken(typeMap)
        val tokens = mutableListOf<Token>()

        for ((str, coordinates) in stringWithCoordinates) {
            val token = converter.convert(str, coordinates)
            tokens.add(token)
        }
        return tokens
    }
}