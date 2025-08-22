package parser.command

import Token
import TokenType
import node.statement.Statement
import parser.Parser

class ExpressionStatementParser : StatementParserCommand {
    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean {
        if (token == null) return false

        val tokenType = token.getType()
        val tokenValue = token.getValue()

        return isNumberLiteral(tokenType) ||
            isStringLiteral(tokenType) ||
            isIdentifier(tokenType) ||
            isOpeningParenthesis(tokenType, tokenValue)
    }

    private fun isNumberLiteral(tokenType: TokenType): Boolean = tokenType == TokenType.NUMBER_LITERAL

    private fun isStringLiteral(tokenType: TokenType): Boolean = tokenType == TokenType.STRING_LITERAL

    private fun isIdentifier(tokenType: TokenType): Boolean = tokenType == TokenType.IDENTIFIER

    private fun isOpeningParenthesis(
        tokenType: TokenType,
        tokenValue: String,
    ): Boolean = tokenType == TokenType.DELIMITERS && tokenValue == "("

    override fun parse(parser: Parser): Statement {
        val expression = parser.getExpressionParser().parseExpression(parser)
        parser.consume(TokenType.DELIMITERS) // ;
        return parser.getNodeBuilder().buildExpressionStatementNode(expression)
    }
}
