package lexer

import Token

sealed interface Lexer {
    fun tokenize(): List<Token>
}