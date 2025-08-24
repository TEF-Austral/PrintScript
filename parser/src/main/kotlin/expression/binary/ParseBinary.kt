package parser.expression.binary

import Token
import node.expression.Expression
import parser.Parser

sealed interface ParseBinary {
    fun parseBinary(
        parser: Parser,
        left: Expression,
        minPrecedence: Int,
    ): Pair<Expression, Parser>

    fun hasValidOperatorToken(parser: Parser): Boolean

    fun meetsMinimumPrecedence(
        parser: Parser,
        minPrecedence: Int,
    ): Boolean

    fun consumeOperator(parser: Parser): Pair<Token, Parser>

    fun parseRightOperand(parser: Parser): Pair<Expression, Parser>

    fun processRightAssociativity(
        parser: Parser,
        right: Expression,
        operator: Token,
    ): Pair<Expression, Parser>

    fun buildBinaryExpression(
        parser: Parser,
        left: Expression,
        operator: Token,
        right: Expression,
    ): Pair<Expression, Parser>

    fun getOperatorPrecedence(token: Token): Int

    fun isOperatorToken(token: Token): Boolean
}
