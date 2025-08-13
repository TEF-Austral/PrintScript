package splitter

import Coordinates

interface Splitter {
    fun split(input: String): List<Pair<String, Coordinates>>
}