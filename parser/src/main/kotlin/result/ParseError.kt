package parser.result

import Coordinates

data class ParseError(
    val message: String,
    val position: Coordinates
)