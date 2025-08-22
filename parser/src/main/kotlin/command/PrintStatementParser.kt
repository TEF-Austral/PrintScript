package parser.command

import Token
import TokenType
import node.statement.Statement
import parser.Parser
import parser.result.ParseResult

class PrintStatementParser : StatementParserCommand {

    override fun canHandle(token: Token?, parser: Parser): Boolean =
        token?.getType() == TokenType.PRINT

    override fun parse(parser: Parser): ParseResult<Statement> {
        // 'print'
        when (val printRes = parser.consumeOrError(TokenType.PRINT)) {
            is ParseResult.Failure -> return printRes
            else -> {}
        }
        // '('
        when (val openRes = parser.consumeOrError(TokenType.DELIMITERS)) {
            is ParseResult.Failure -> return openRes
            else -> {}
        }
        // expression
        val expression = parser.getExpressionParser().parseExpression(parser)
        // ')'
        when (val closeRes = parser.consumeOrError(TokenType.DELIMITERS)) {
            is ParseResult.Failure -> return closeRes
            else -> {}
        }
        // ';'
        when (val semiRes = parser.consumeOrError(TokenType.DELIMITERS)) {
            is ParseResult.Failure -> return semiRes
            else -> {}
        }
        val stmt = parser.getNodeBuilder().buildPrintStatementNode(expression)
        return ParseResult.Success(stmt)
    }
}