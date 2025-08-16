package parser.expression

import node.Expression
import parser.RecursiveDescentParser

class DefaultExpressionParser : ExpressionParser {

    override fun parseExpression(parser: RecursiveDescentParser): Expression {
        val left = parsePrimary(parser)
        return parseBinary(parser, left, 0)
    }

    override fun parsePrimary(parser: RecursiveDescentParser): Expression {
        val current = parser.getCurrentToken() ?: throw ParseException("Unexpected end of input")

        return when (current.getType()) {
            "NUMBER", "STRING", "BOOLEAN" -> {
                parser.advance()
                parser.getNodeBuilder().buildLiteralExpressionNode(current)
            }

            "IDENTIFIER" -> {
                parser.advance()
                parser.getNodeBuilder().buildIdentifierNode(current)
            }

            "LEFT_PAREN" -> {
                parser.advance() // consume '('
                val expr = parseExpression(parser)
                parser.consume("RIGHT_PAREN", "Expected ')' after expression")
                expr
            }

            else -> throw ParseException("Expected expression but found ${current.getType()}")
        }
    }

    override fun parseBinary(parser: RecursiveDescentParser, left: Expression, minPrec: Int): Expression {
        var result = left

        while (true) {
            val current = parser.getCurrentToken()
            if (current == null || !isOperator(current.getType())) break

            val prec = getOperatorPrecedence(current.getType())
            if (prec < minPrec) break

            val operator = parser.advance()!!
            var right = parsePrimary(parser)

            // Manejar asociatividad y precedencia correctamente
            val nextToken = parser.getCurrentToken()
            if (nextToken != null && isOperator(nextToken.getType())) {
                val nextPrec = getOperatorPrecedence(nextToken.getType())
                if (prec < nextPrec || (prec == nextPrec && isRightAssociative(operator.getType()))) {
                    right = parseBinary(parser, right, prec + 1)
                }
            }

            result = parser.getNodeBuilder().buildBinaryExpressionNode(result, operator, right)
        }

        return result
    }

    private fun isRightAssociative(type: String): Boolean {
        // La mayoría de operadores son left-associative
        // Aquí podrías agregar operadores right-associative como potenciación
        return false
    }

    private fun isOperator(type: String): Boolean {
        return type in listOf(
            "PLUS",
            "MINUS",
            "MULTIPLY",
            "DIVIDE",
            "EQUALS",
            "NOT_EQUALS",
            "LESS_THAN",
            "GREATER_THAN"
        )
    }

    private fun getOperatorPrecedence(type: String): Int {
        return when (type) {
            "EQUALS", "NOT_EQUALS" -> 1
            "LESS_THAN", "GREATER_THAN" -> 2
            "PLUS", "MINUS" -> 3
            "MULTIPLY", "DIVIDE" -> 4
            else -> 0
        }
    }
}