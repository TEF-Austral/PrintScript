package parser.expression

import node.expression.EmptyExpression
import node.expression.Expression
import parser.Parser
import parser.expression.binary.ParseBinary

class ExpressionParser(
    private val expressionBuilder: TokenToExpression,
    private val binaryBuilder: ParseBinary,
) {
    fun parseExpression(parser: Parser): Pair<Expression, Parser> {
        val (left, parserAfterPrimary) = parsePrimary(parser)
        return parseBinary(parserAfterPrimary, left, 0)
    }

    fun parsePrimary(parser: Parser): Pair<Expression, Parser> {
        val current = parser.getCurrentToken() ?: return EmptyExpression() to parser
        return expressionBuilder.build(parser, current)
    }

    fun parseBinary(
        parser: Parser,
        left: Expression,
        minPrecedence: Int,
    ): Pair<Expression, Parser> = binaryBuilder.parseBinary(parser, left, minPrecedence)
}