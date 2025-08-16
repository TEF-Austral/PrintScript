package parser.expression

import Token
import TokenType
import node.EmptyExpression
import node.Expression
import parser.RecursiveDescentParser

class DefaultExpressionParser : ExpressionParser {

    override fun parseExpression(parser: RecursiveDescentParser): Expression {
        val left = parsePrimary(parser)
        return parseBinary(parser, left, 0)
    }

    override fun parsePrimary(parser: RecursiveDescentParser): Expression {
        val current = parser.getCurrentToken() ?: return EmptyExpression()

        return when (current.getType()) {
            TokenType.NUMBER_LITERAL,
            TokenType.STRING_LITERAL -> {
                parser.advance()
                parser.getNodeBuilder().buildLiteralExpressionNode(current)
            }

            TokenType.IDENTIFIER -> {
                parser.advance()
                parser.getNodeBuilder().buildIdentifierNode(current)
            }

            TokenType.DELIMITERS -> {
                if (current.getValue() == "(") {
                    parser.advance() // consume '('
                    val expr = parseExpression(parser)
                    expectDelimiter(parser, ")") // no-throw, best-effort
                    expr
                } else {
                    EmptyExpression()
                }
            }

            else -> EmptyExpression()
        }
    }

    override fun parseBinary(parser: RecursiveDescentParser, left: Expression, minPrec: Int): Expression {
        var result = left

        while (true) {
            val current = parser.getCurrentToken() ?: break
            if (!isOperator(current)) break

            val prec = getOperatorPrecedence(current)
            if (prec < minPrec) break

            val operator = parser.advance()!!
            var right = parsePrimary(parser)

            val next = parser.getCurrentToken()
            if (next != null && isOperator(next)) {
                val nextPrec = getOperatorPrecedence(next)
                if (prec < nextPrec || (prec == nextPrec && isRightAssociative(operator))) {
                    right = parseBinary(parser, right, prec + 1)
                }
            }

            result = parser.getNodeBuilder().buildBinaryExpressionNode(result, operator, right)
        }

        return result
    }

    // Helpers

    private fun expectDelimiter(parser: RecursiveDescentParser, expected: String) {
        val t = parser.getCurrentToken()
        if (t != null && t.getType() == TokenType.DELIMITERS && t.getValue() == expected) {
            parser.consume(TokenType.DELIMITERS)
        }
        // else: no-op (no exceptions)
    }

    private fun isRightAssociative(token: Token): Boolean {
        // Customize if adding right-associative operators like '^'
        return false
    }

    private fun isOperator(token: Token): Boolean {
        return when (token.getType()) {
            TokenType.OPERATORS,
            TokenType.COMPARISON,
            TokenType.LOGICAL_OPERATORS -> true

            else -> false
        }
    }

    private fun getOperatorPrecedence(token: Token): Int {
        return when (token.getType()) {
            TokenType.LOGICAL_OPERATORS -> 1 // ||, &&
            TokenType.COMPARISON -> 2       // ==, !=, <, >, <=, >=
            TokenType.OPERATORS -> when (token.getValue()) {
                "+", "-" -> 3
                "*", "/", "%" -> 4
                else -> 0
            }

            else -> 0
        }
    }
}

