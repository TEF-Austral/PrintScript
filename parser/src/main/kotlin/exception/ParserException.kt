package parser.exception

import coordinates.Coordinates

class ParserException(
    message: String,
    val coordinates: Coordinates?,
) : Exception(message)
