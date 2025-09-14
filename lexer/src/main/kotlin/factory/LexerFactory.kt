package factory

import Lexer
import converter.specific.StringToTokenConverter
import type.Version

interface LexerFactory {
    fun createVersionOne(): Lexer

    fun createVersionOnePointOne(): Lexer

    fun createCustom(
        specialChars: List<Char>,
        customConverters: List<StringToTokenConverter>,
    ): Lexer

    fun createLexerWithVersion(version: Version): Lexer
}
