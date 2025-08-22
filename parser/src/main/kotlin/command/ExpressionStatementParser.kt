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

    override fun parse(parser: Parser): Pair<ParseResult<Statement>, Parser> {
        // parse the expression
        val (expr, p1) = parser.getExpressionParser().parseExpression(parser)
        // consume the semicolon
        val (semiRes, p2) = p1.consumeOrError(TokenType.DELIMITERS)
        if (semiRes is ParseResult.Failure) return Pair(semiRes, p2)
        // build and return the expression statement
        val stmt = p2.getNodeBuilder().buildExpressionStatementNode(expr)
        return Pair(ParseResult.Success(stmt), p2)
    }
}