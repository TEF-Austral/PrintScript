package factory

import Lexer
import converter.specific.StringToTokenConverter
import java.io.Reader
import type.Version

interface LexerFactory {
    fun createVersionOne(reader: Reader): Lexer

    fun createVersionOnePointOne(reader: Reader): Lexer

    fun createCustom(
        specialChars: List<Char>,
        customConverters: List<StringToTokenConverter>,
        reader: Reader,
    ): Lexer

    fun createLexerWithVersion(
        version: Version,
        reader: Reader,
    ): Lexer

    fun createLexerWithVersionAndBufferSize(
        reader: Reader,
        size: Int,
    ): Lexer
}
