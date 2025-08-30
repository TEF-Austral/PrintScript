package parser.command

import Token
import node.Statement
import parser.Parser
import parser.result.ParseResult
import type.CommonTypes

class ExpressionStatementParser : StatementParserCommand {
    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean {
        if (token == null) return false
        val type = token.getType()
        val value = token.getValue()
        return type == CommonTypes.NUMBER_LITERAL ||
            type == CommonTypes.STRING_LITERAL ||
            type == CommonTypes.IDENTIFIER ||
            (type == CommonTypes.DELIMITERS && value == "(")
    }

    override fun parse(parser: Parser): Pair<ParseResult<Statement>, Parser> {
        // parse the expression
        val (expr, p1) = parser.getExpressionParser().parseExpression(parser)
        // consume the semicolon
        val (semiRes, p2) = p1.consumeOrError(CommonTypes.DELIMITERS)
        if (semiRes is ParseResult.Failure) return Pair(semiRes, p2)
        // build and return the expression statement
        val stmt = p2.getNodeBuilder().buildExpressionStatementNode(expr)
        return Pair(ParseResult.Success(stmt), p2)
    }
}
