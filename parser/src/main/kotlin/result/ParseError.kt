package parser.result

import type.Coordinates

data class ParseError(
    val message: String,
    val position: Coordinates,
)
