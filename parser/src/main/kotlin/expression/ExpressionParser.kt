package parser.expression

import node.EmptyExpression
import parser.Parser
import parser.expression.binary.ParseBinary
import parser.result.ExpressionBuiltResult
import parser.result.ExpressionResult

class ExpressionParser(
    val expressionBuilder: TokenToExpression,
    val binaryBuilder: ParseBinary,
) {
    fun parseExpression(parser: Parser): ExpressionResult = parseBinary(parsePrimary(parser))

    fun parsePrimary(parser: Parser): ExpressionResult {
        val current = parser.peak() ?: return ExpressionBuiltResult(parser, EmptyExpression())
        return expressionBuilder.build(parser, current)
    }

    fun parseBinary(left: ExpressionResult): ExpressionResult = binaryBuilder.parseBinary(left, 0)
}
