package parser.result

import coordinates.Coordinates
import node.EmptyStatement
import node.Program
import parser.Parser

class FailedProgram(
    private val parser: Parser,
    private val message: String,
    private val coordinates: Coordinates? = null,
) : FinalResult {
    override fun getProgram(): Program = Program(listOf(EmptyStatement()))

    override fun isSuccess(): Boolean = false

    override fun message(): String = message

    override fun getParser(): Parser = parser

    override fun getCoordinates(): Coordinates? = coordinates
}
