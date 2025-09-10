package factory

import Lexer
import converter.specific.StringToTokenConverter

interface LexerFactory {
    fun createVersionOne(): Lexer

    fun createVersionOnePointOne(): Lexer

    fun createCustom(
        specialChars: List<Char>,
        customConverters: List<StringToTokenConverter>,
    ): Lexer
}
