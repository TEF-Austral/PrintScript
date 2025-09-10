import converter.TokenConverter
import stringSplitter.Splitter

class DefaultLexer(
    private val splitter: Splitter,
    private val tokenConverter: TokenConverter,
) : Lexer {
    override fun tokenize(reader: Reader): List<Token> {
        val stringWithCoordinates = splitter.split(reader.read())

        return stringWithCoordinates.map { (str, coordinates) ->
            tokenConverter.convert(str, coordinates)
        }
    }
}
