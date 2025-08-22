package parser.result

import Coordinates

sealed class ParseResult<out T> {
    data class Success<out T>(
        val value: T,
    ) : ParseResult<T>()

    data class Failure(
        val error: ParseError,
    ) : ParseResult<Nothing>()
}

data class ParseError(
    val message: String,
    val position: Coordinates,
)
