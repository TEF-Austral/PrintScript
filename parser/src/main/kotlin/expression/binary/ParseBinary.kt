package parser.expression.binary

import Token
import node.expression.Expression
import parser.Parser

sealed interface ParseBinary {

    fun parseBinary(parser: Parser, left: Expression, minPrecedence: Int): Expression

    fun hasValidOperatorToken(parser: Parser): Boolean

    fun meetsMinimumPrecedence(parser: Parser, minPrecedence: Int): Boolean

    fun consumeOperator(parser: Parser): Token

    fun parseRightOperand(parser: Parser): Expression

    fun processRightAssociativity(parser: Parser, right: Expression, operator: Token): Expression

    fun buildBinaryExpression(parser: Parser, left: Expression, operator: Token, right: Expression): Expression

    fun getOperatorPrecedence(token: Token): Int

    fun isOperatorToken(token: Token): Boolean
}