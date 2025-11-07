package parser.result

import coordinates.Coordinates
import node.Program

interface FinalResult : ParserResult {
    fun getProgram(): Program

    fun getCoordinates(): Coordinates?
}
