package parser.expression.binary

import Token
import node.expression.Expression
import parser.Parser
import parser.expression.TokenToExpression

class DefaultParseBinary(private val tokenToExpression: TokenToExpression): ParseBinary {

    override fun parseBinary(parser: Parser, left: Expression, minPrecedence: Int): Expression {
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

    override fun hasValidOperatorToken(parser: Parser): Boolean {
        val currentToken = parser.getCurrentToken() ?: return false
        return isOperatorToken(currentToken)
    }

    override fun meetsMinimumPrecedence(parser: Parser, minPrecedence: Int): Boolean {
        val currentToken = parser.getCurrentToken()!!
        val precedence = getOperatorPrecedence(currentToken)
        return precedence >= minPrecedence
    }

    override fun consumeOperator(parser: Parser): Token {
        return parser.advance()!!
    }

    override fun parseRightOperand(parser: Parser): Expression {
        val currentToken = parser.getCurrentToken()!!
        return tokenToExpression.build(parser, currentToken)
    }

    override fun processRightAssociativity(parser: Parser, right: Expression, operator: Token): Expression {
        val nextToken = parser.getCurrentToken()

        if (nextToken == null || !isOperatorToken(nextToken)) {
            return right
        }

        val currentPrecedence = getOperatorPrecedence(operator)
        val nextPrecedence = getOperatorPrecedence(nextToken)

        val shouldParseRecursively = when {
            nextPrecedence > currentPrecedence -> true
            nextPrecedence == currentPrecedence -> true
            else -> false
        }

        return if (shouldParseRecursively) {
            parseBinary(parser, right, currentPrecedence + 1)
        } else {
            right
        }
    }

    override fun buildBinaryExpression(parser: Parser, left: Expression, operator: Token, right: Expression): Expression {
        return parser.getNodeBuilder().buildBinaryExpressionNode(left, operator, right)
    }

    override fun isOperatorToken(token: Token): Boolean {
        return when (token.getType()) {
            TokenType.OPERATORS,
            TokenType.COMPARISON,
            TokenType.LOGICAL_OPERATORS -> true
            else -> false
        }
    }

    override fun getOperatorPrecedence(token: Token): Int {
        return when (token.getType()) {
            TokenType.LOGICAL_OPERATORS -> 1 // ||, &&
            TokenType.COMPARISON -> 2       // ==, !=, <, >, <=, >=
            TokenType.OPERATORS -> when (token.getValue()) {
                "+", "-" -> 3
                "*", "/", "%" -> 4
                "^" -> 5
                else -> 0
            }
            else -> 0
        }
    }
}