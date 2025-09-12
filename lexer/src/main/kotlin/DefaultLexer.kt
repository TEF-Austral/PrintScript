import converter.TokenConverter
import converter.string.StreamingSplitter

class DefaultLexer(
    private val tokenConverter: TokenConverter,
    private val splitter: StreamingSplitter,
) : Lexer {

    override fun next(): result.LexerResult? {
        val splitterResult = splitter.next()
        return if (splitterResult != null) {
            val token = tokenConverter.convert(splitterResult.string, splitterResult.coordinates)
            val nextLexer = DefaultLexer(tokenConverter, splitterResult.splitter)
            result.LexerResult(token, nextLexer)
        } else {
            null
        }
    }


    override fun peek(): Token? {
        val splitterResult = splitter.next()
        return if (splitterResult != null) {
            tokenConverter.convert(splitterResult.string, splitterResult.coordinates)
        } else {
            null
        }
    }
}
