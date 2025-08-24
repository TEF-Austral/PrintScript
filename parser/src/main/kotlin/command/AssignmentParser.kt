package parser.command

import Token
import TokenType
import node.statement.Statement
import parser.Parser
import parser.result.ParseResult

class AssignmentParser : StatementParserCommand {
    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean {
        if (token?.getType() != TokenType.IDENTIFIER) return false
        return parser.advance().getCurrentToken()?.getType() == TokenType.ASSIGNMENT
    }

    override fun parse(parser: Parser): Pair<ParseResult<Statement>, Parser> {
        val (idRes, p1) = parser.consumeOrError(TokenType.IDENTIFIER)
        if (idRes is ParseResult.Failure) return Pair(idRes, p1)
        val idToken = (idRes as ParseResult.Success).value

        val (eqRes, p2) = p1.consumeOrError(TokenType.ASSIGNMENT)
        if (eqRes is ParseResult.Failure) return Pair(eqRes, p2)

        // parse the expression and get the updated parser
        val (valueExpr, p3) = p2.getExpressionParser().parseExpression(p2)

        val (semiRes, p4) = p3.consumeOrError(TokenType.DELIMITERS)
        if (semiRes is ParseResult.Failure) return Pair(semiRes, p4)

        // pass only the Expression to the builder
        val stmt =
            p4
                .getNodeBuilder()
                .buildAssignmentStatementNode(idToken, valueExpr)
        return Pair(ParseResult.Success(stmt), p4)
    }
}
