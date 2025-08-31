package parser.result

import node.Statement
import parser.Parser

class StatementBuiltResult(
    private val parser: Parser,
    private val statement: Statement,
) : StatementResult {
    override fun getStatement(): Statement = statement

    override fun isSuccess(): Boolean = true

    override fun message(): String = "Success"

    override fun getParser(): Parser = parser
}
