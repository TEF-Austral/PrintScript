package converter

import Coordinates
import Token

sealed interface TokenConverter {
    fun convert(input: String, position: Coordinates): Token
}