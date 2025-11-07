package parser.result

import Token
import coordinates.Coordinates
import node.Expression
import parser.Parser

class SemanticError(
    private val message: String,
    private val identifier: Token,
    private val dataType: Token,
    private val initialValue: Expression?,
    private val parser: Parser,
    private val coordinates: Coordinates? = null,
) : SemanticResult {
    override fun isSuccess(): Boolean = false

    override fun message(): String = message

    override fun getParser(): Parser = parser

    override fun identifier(): Token = identifier

    override fun dataType(): Token = dataType

    override fun initialValue(): Expression? = initialValue

    override fun getCoordinates(): Coordinates? = coordinates
}
