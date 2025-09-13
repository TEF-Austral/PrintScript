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

    override fun createVersionOne(reader: Reader): Lexer {
        val converterList = converterFactory.createVersionOne()
        val splitter = StringSplitterFactory.createStreamingSplitter(reader)
        return DefaultLexer(converterList, splitter)
    }

    override fun createVersionOnePointOne(reader: Reader): Lexer {
        val converterList = converterFactory.createVersionOnePointOne()
        val splitter = StringSplitterFactory.createStreamingSplitter(reader)
        return DefaultLexer(converterList, splitter)
    }

    override fun createCustom(
        specialChars: List<Char>,
        customConverters: List<StringToTokenConverter>,
        reader: Reader,
    ): Lexer {
        val splitter = StringSplitterFactory.createStreamingSplitter(reader)
        val converters = converterFactory.createCustom(customConverters)
        return DefaultLexer(converters, splitter)
    }

    override fun createLexerWithVersion(
        version: Version,
        reader: Reader,
    ): Lexer =
        when (version){
            VERSION_1_0 -> createVersionOne(reader)
            VERSION_1_1 -> createVersionOnePointOne(reader)
        }
}
