package parser.result


sealed class ParseResult<out T> {
    data class Success<out T>(val value: T) : ParseResult<T>()
    data class Failure(val error: ParseError) : ParseResult<Nothing>()
}
