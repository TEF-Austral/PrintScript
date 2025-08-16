package parser.expression

import node.Expression
import parser.RecursiveDescentParser

sealed interface ExpressionParser {
    fun parseExpression(parser: RecursiveDescentParser): Expression
    fun parsePrimary(parser: RecursiveDescentParser): Expression
    fun parseBinary(parser: RecursiveDescentParser, left: Expression, minPrec: Int): Expression
}
