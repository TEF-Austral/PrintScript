package converter

import type.Coordinates
import Token

interface TokenConverter {
    fun convert(
        input: String,
        position: Coordinates,
    ): Token
}
