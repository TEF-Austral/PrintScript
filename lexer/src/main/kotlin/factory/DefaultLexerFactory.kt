package factory

import DefaultLexer
import Lexer
import converter.specific.StringToTokenConverter
import java.io.Reader
import type.Version
import type.Version.VERSION_1_0
import type.Version.VERSION_1_1

data class DefaultLexerFactory(
    val splitterFactory: StringSplitterFactory,
    val converterFactory: ConverterFactory,
) : LexerFactory {

    override fun createVersionOne(
        reader: Reader,
        size: Int,
    ): Lexer {
        val converterList = converterFactory.createVersionOne()
        val splitter = StringSplitterFactory.createStreamingSplitter(reader, size)
        return DefaultLexer(converterList, splitter)
    }

    override fun createVersionOnePointOne(
        reader: Reader,
        size: Int,
    ): Lexer {
        val converterList = converterFactory.createVersionOnePointOne()
        val splitter = StringSplitterFactory.createStreamingSplitter(reader, size)
        return DefaultLexer(converterList, splitter)
    }

    override fun createCustom(
        specialChars: List<Char>,
        customConverters: List<StringToTokenConverter>,
        reader: Reader,
        size: Int,
    ): Lexer {
        val splitter = StringSplitterFactory.createStreamingSplitter(reader, size)
        val converters = converterFactory.createCustom(customConverters)
        return DefaultLexer(converters, splitter)
    }

    override fun createLexerWithVersion(
        version: Version,
        reader: Reader,
        size: Int,
    ): Lexer =
        when (version){
            VERSION_1_0 -> createVersionOne(reader, size)
            VERSION_1_1 -> createVersionOnePointOne(reader, size)
        }

    override fun createLexerWithVersionAndBufferSize(
        reader: Reader,
        size: Int,
        version: Version,
    ): Lexer =
        when (version){
            VERSION_1_0 -> createVersionOne(reader, size)
            VERSION_1_1 -> createVersionOnePointOne(reader, size)
        }
}
