package parser.result

import coordinates.Coordinates
import node.EmptyStatement
import node.Statement
import parser.Parser

class StatementErrorResult(
    private val parser: Parser,
    private val message: String,
    private val coordinates: Coordinates? = null,
) : StatementResult {
    override fun getStatement(): Statement = EmptyStatement()

    override fun isSuccess(): Boolean = false

    override fun message(): String = message

    override fun getParser(): Parser = parser

    fun getCoordinates(): Coordinates? = coordinates
}
