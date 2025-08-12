package lexer

import lexer.reader.Reader
import token.Token
import token.TokenConverter

sealed interface Lexer {
    fun tokenize(): List<Token>
}
class PrintScriptLexer(val reader: Reader, val splitter: Splitter, val tokenConverter: TokenConverter): Lexer {

    override fun tokenize(): List<Token> {
        val stringWithCoordinates = splitter.split(reader.read())
        val tokens = mutableListOf<Token>()

        for ((str, coordinates) in stringWithCoordinates) {
            val token = tokenConverter.convert(str, coordinates)
            tokens.add(token)
        }
        return tokens
    }
}