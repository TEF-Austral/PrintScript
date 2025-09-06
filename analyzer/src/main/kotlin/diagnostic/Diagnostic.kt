package diagnostic

import coordinates.Coordinates

data class Diagnostic(
    val message: String,
    val position: Coordinates,
)
