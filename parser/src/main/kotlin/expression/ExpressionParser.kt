package parser.expression

import node.expression.EmptyExpression
import node.expression.Expression
import parser.Parser
import parser.expression.binary.ParseBinary

class ExpressionParser(val expressionBuilder: TokenToExpression, val binaryBuilder: ParseBinary) {

    fun parseExpression(parser: Parser): Expression {
        val left = parsePrimary(parser)
        return parseBinary(parser, left)
    }

    fun parsePrimary(parser: Parser): Expression { // composite
        val current = parser.getCurrentToken() ?: return EmptyExpression()
        return expressionBuilder.build(parser, current)
    }

    fun parseBinary(parser: Parser, left: Expression): Expression {
        return binaryBuilder.parseBinary(parser, left, 0)
    }
}