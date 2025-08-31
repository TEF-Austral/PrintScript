package parser.result

import node.Expression
import parser.Parser

class ExpressionBuiltResult(
    private val parser: Parser,
    private val expression: Expression,
) : ExpressionResult {
    override fun getExpression(): Expression = expression

    override fun isSuccess(): Boolean = true

    override fun message(): String = "Success"

    override fun getParser(): Parser = parser
}
