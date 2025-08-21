import converter.TokenConverter
import reader.Reader
import splitter.Splitter

class Lexer(val splitter: Splitter, val tokenConverter: TokenConverter) {

    fun tokenize(reader: Reader): List<Token> {
        val stringWithCoordinates = splitter.split(reader.read())
        val tokens = mutableListOf<Token>()

        for ((str, coordinates) in stringWithCoordinates) {
            val token = tokenConverter.convert(str, coordinates)
            tokens.add(token)
        }
        return tokens
    }
}