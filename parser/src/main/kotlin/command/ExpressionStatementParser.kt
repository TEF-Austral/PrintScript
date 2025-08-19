package parser.command

import Token
import TokenType
import node.Statement
import parser.Parser
import parser.RecursiveDescentParser

class ExpressionStatementParser : StatementParserCommand {

    override fun canHandle(token: Token?, parser: Parser): Boolean {
        if (token == null) return false

        val tokenType = token.getType()
        val tokenValue = token.getValue()

        return isNumberLiteral(tokenType) || isStringLiteral(tokenType) || isIdentifier(tokenType) ||
               isOpeningParenthesis(tokenType, tokenValue)
    }

    private fun isNumberLiteral(tokenType: TokenType): Boolean {
        return tokenType == TokenType.NUMBER_LITERAL
    }

    private fun isStringLiteral(tokenType: TokenType): Boolean {
        return tokenType == TokenType.STRING_LITERAL
    }

    private fun isIdentifier(tokenType: TokenType): Boolean {
        return tokenType == TokenType.IDENTIFIER
    }

    private fun isOpeningParenthesis(tokenType: TokenType, tokenValue: String): Boolean {
        return tokenType == TokenType.DELIMITERS && tokenValue == "("
    }

    override fun parse(parser: RecursiveDescentParser): Statement {
        val expression = parser.getExpressionParser().parseExpression(parser)
        parser.consume(TokenType.DELIMITERS) // ;
        return parser.getNodeBuilder().buildExpressionStatementNode(expression)
    }
}