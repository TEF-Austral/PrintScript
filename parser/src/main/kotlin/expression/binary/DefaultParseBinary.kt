package parser.expression.binary

import Token
import TokenType
import node.Expression
import parser.Parser
import parser.expression.TokenToExpression

class DefaultParseBinary(
    private val tokenToExpression: TokenToExpression,
) : ParseBinary {
    override fun parseBinary(
        parser: Parser,
        left: Expression,
        minPrecedence: Int,
    ): Pair<Expression, Parser> {
        var p = parser
        var result = left

        while (true) {
            if (!hasValidOperatorToken(p)) break
            if (!meetsMinimumPrecedence(p, minPrecedence)) break

            val (operator, p1) = consumeOperator(p)
            val (rightOperand, p2) = parseRightOperand(p1)
            val (processedRight, p3) = processRightAssociativity(p2, rightOperand, operator)
            val (newResult, p4) = buildBinaryExpression(p3, result, operator, processedRight)

            result = newResult
            p = p4
        }

        return Pair(result, p)
    }

    override fun hasValidOperatorToken(parser: Parser): Boolean {
        val current = parser.getCurrentToken() ?: return false
        return isOperatorToken(current)
    }

    override fun meetsMinimumPrecedence(
        parser: Parser,
        minPrecedence: Int,
    ): Boolean {
        val current = parser.getCurrentToken()!!
        return getOperatorPrecedence(current) >= minPrecedence
    }

    override fun consumeOperator(parser: Parser): Pair<Token, Parser> {
        val operator = parser.getCurrentToken()!!
        return Pair(operator, parser.advance())
    }

    override fun parseRightOperand(parser: Parser): Pair<Expression, Parser> {
        val current = parser.getCurrentToken()!!
        return tokenToExpression.build(parser, current)
    }

    override fun processRightAssociativity(
        parser: Parser,
        right: Expression,
        operator: Token,
    ): Pair<Expression, Parser> {
        val next = parser.getCurrentToken()
        if (next == null || !isOperatorToken(next)) {
            return Pair(right, parser)
        }

        val currPrec = getOperatorPrecedence(operator)
        val nextPrec = getOperatorPrecedence(next)
        val shouldRecurse = nextPrec >= currPrec

        return if (shouldRecurse) {
            parseBinary(parser, right, currPrec + 1)
        } else {
            Pair(right, parser)
        }
    }

    override fun buildBinaryExpression(
        parser: Parser,
        left: Expression,
        operator: Token,
        right: Expression,
    ): Pair<Expression, Parser> {
        val exprNode = parser.getNodeBuilder().buildBinaryExpressionNode(left, operator, right)
        return Pair(exprNode, parser)
    }

    override fun isOperatorToken(token: Token): Boolean =
        when (token.getType()) {
            TokenType.OPERATORS,
            TokenType.COMPARISON,
            TokenType.LOGICAL_OPERATORS,
            -> true
            else -> false
        }

    override fun getOperatorPrecedence(token: Token): Int =
        when (token.getType()) {
            TokenType.LOGICAL_OPERATORS -> 1
            TokenType.COMPARISON -> 2
            TokenType.OPERATORS ->
                when (token.getValue()) {
                    "+", "-" -> 3
                    "*", "/", "%" -> 4
                    "^" -> 5
                    else -> 0
                }
            else -> 0
        }
}
