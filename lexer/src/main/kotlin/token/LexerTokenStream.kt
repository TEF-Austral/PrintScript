package streaming

import Lexer // Importa tu interfaz Lexer
import Token
import TokenStream
import result.StreamResult

/**
 * Implementación inmutable de TokenStream que envuelve un Lexer.
 */
class LexerTokenStream(
    private val lexer: Lexer,
) : TokenStream {

    private val currentToken: Token? by lazy { lexer.peek() }

    override fun isAtEnd(): Boolean = currentToken == null

    override fun peak(): Token? = currentToken

    override fun next(): StreamResult? {
        val lexerResult = lexer.next() ?: return null

        val nextStream = LexerTokenStream(lexerResult.lexer)

        return StreamResult(lexerResult.token, nextStream)
    }
}
