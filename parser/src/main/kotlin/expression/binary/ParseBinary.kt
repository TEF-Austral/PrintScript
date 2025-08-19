package parser.expression.binary

import Token
import node.expression.Expression
import parser.Parser

sealed interface ParseBinary {
    // TODO sacar template anti patron
    fun parseBinary(parser: Parser, left: Expression, minPrecedence: Int): Expression {
        var result = left

        while (true) {
            if (!hasValidOperatorToken(parser)) break
            if (!meetsMinimumPrecedence(parser, minPrecedence)) break
            val operator = consumeOperator(parser)
            val rightOperand = parseRightOperand(parser)
            val processedRight = processRightAssociativity(parser, rightOperand, operator)

            result = buildBinaryExpression(parser, result, operator, processedRight)
        }

        return result
    }
    fun hasValidOperatorToken(parser: Parser): Boolean

    fun meetsMinimumPrecedence(parser: Parser, minPrecedence: Int): Boolean

    fun consumeOperator(parser: Parser): Token

    fun parseRightOperand(parser: Parser): Expression

    fun processRightAssociativity(parser: Parser, right: Expression, operator: Token): Expression

    fun buildBinaryExpression(parser: Parser, left: Expression, operator: Token, right: Expression): Expression

    fun getOperatorPrecedence(token: Token): Int

    fun isOperatorToken(token: Token): Boolean
}