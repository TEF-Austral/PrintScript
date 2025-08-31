package parser.result

import node.EmptyExpression
import node.Expression
import parser.Parser

class ExpressionErrorResult(private val message: String, private val parser: Parser): ExpressionResult {
    override fun getExpression(): Expression = EmptyExpression()

    override fun isSuccess(): Boolean = false

    override fun message(): String = message

    override fun getParser(): Parser = parser
}