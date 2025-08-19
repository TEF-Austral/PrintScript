package parser.expression

import node.expression.Expression
import parser.Parser

sealed interface ExpressionParser {
    fun parseExpression(parser: Parser): Expression
    fun parsePrimary(parser: Parser): Expression
    fun parseBinary(parser: Parser, left: Expression): Expression
}
