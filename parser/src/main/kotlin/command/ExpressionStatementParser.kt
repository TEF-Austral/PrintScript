package parser.command

import Token
import TokenType
import node.statement.Statement
import parser.Parser
import parser.result.ParseResult

class ExpressionStatementParser : StatementParserCommand {

    override fun canHandle(token: Token?, parser: Parser): Boolean {
        if (token == null) return false
        val type = token.getType()
        val value = token.getValue()
        return type == TokenType.NUMBER_LITERAL ||
                type == TokenType.STRING_LITERAL ||
                type == TokenType.IDENTIFIER ||
                (type == TokenType.DELIMITERS && value == "(")
    }

    override fun parse(parser: Parser): ParseResult<Statement> {
        val expression = parser.getExpressionParser().parseExpression(parser)
        when (val semiRes = parser.consumeOrError(TokenType.DELIMITERS)) {
            is ParseResult.Failure -> return semiRes
            else -> {}
        }
        val stmt = parser.getNodeBuilder().buildExpressionStatementNode(expression)
        return ParseResult.Success(stmt)
    }
}