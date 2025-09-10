package factory

import DefaultLexer
import Lexer
import converter.specific.StringToTokenConverter

data class DefaultLexerFactory(
    val splitterFactory: StringSplitterFactory,
    val converterFactory: ConverterFactory,
) : LexerFactory {
    override fun createVersionOne(): Lexer {
        val converterList = converterFactory.createVersionOne()
        val splitter = splitterFactory.createDefaultsSplitter()
        return DefaultLexer(splitter, converterList)
    }

    override fun createVersionOnePointOne(): Lexer {
        val splitter = splitterFactory.createDefaultsSplitter()
        val converterList = converterFactory.createVersionOnePointOne()
        return DefaultLexer(splitter, converterList)
    }

    override fun createCustom(
        specialChars: List<Char>,
        customConverters: List<StringToTokenConverter>,
    ): Lexer {
        val splitter = splitterFactory.createCustomSplitter(specialChars)
        val converters = converterFactory.createCustom(customConverters)
        return DefaultLexer(splitter, converters)
    }
}
