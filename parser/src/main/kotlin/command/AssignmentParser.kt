package parser.command

import Token
import node.statement.Statement
import parser.Parser
import parser.result.ParseResult

class AssignmentParser : StatementParserCommand {
    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean {
        if (token?.getType() != TokenType.IDENTIFIER) return false
        val pos = parser.current
        parser.advance()
        val isAssign = parser.check(TokenType.ASSIGNMENT)
        parser.current = pos
        return isAssign
    }

    override fun parse(parser: Parser): ParseResult<Statement> {
        // identifier
        when (val idRes = parser.consumeOrError(TokenType.IDENTIFIER)) {
            is ParseResult.Failure -> return idRes
            is ParseResult.Success -> {
                val idToken = idRes.value

                // '='
                when (val eqRes = parser.consumeOrError(TokenType.ASSIGNMENT)) {
                    is ParseResult.Failure -> return eqRes
                    is ParseResult.Success -> {
                        // value expression
                        val valueExpr = parser.getExpressionParser().parseExpression(parser)

                        // ';'
                        return when (val semiRes = parser.consumeOrError(TokenType.DELIMITERS)) {
                            is ParseResult.Failure -> semiRes
                            is ParseResult.Success ->
                                ParseResult.Success(
                                    parser.getNodeBuilder().buildAssignmentStatementNode(idToken, valueExpr),
                                )
                        }
                    }
                }
            }
        }
    }
}
