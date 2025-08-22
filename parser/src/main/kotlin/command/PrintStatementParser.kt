package parser.command

import Token
import TokenType
import node.statement.Statement
import parser.Parser
import parser.result.ParseResult

class PrintStatementParser : StatementParserCommand {

    override fun canHandle(token: Token?, parser: Parser): Boolean =
        token?.getType() == TokenType.PRINT

    override fun parse(parser: Parser): Pair<ParseResult<Statement>, Parser> {
        var p = parser

        // 'print'
        val (printRes, p1) = p.consumeOrError(TokenType.PRINT)
        if (printRes is ParseResult.Failure) return Pair(printRes, p1)
        p = p1

        // '('
        val (openRes, p2) = p.consumeOrError(TokenType.DELIMITERS)
        if (openRes is ParseResult.Failure) return Pair(openRes, p2)
        p = p2

        // expression
        val (expr, p3) = p.getExpressionParser().parseExpression(p)
        p = p3

        // ')'
        val (closeRes, p4) = p.consumeOrError(TokenType.DELIMITERS)
        if (closeRes is ParseResult.Failure) return Pair(closeRes, p4)
        p = p4

        // ';'
        val (semiRes, p5) = p.consumeOrError(TokenType.DELIMITERS)
        if (semiRes is ParseResult.Failure) return Pair(semiRes, p5)
        p = p5

        val stmt = p.getNodeBuilder().buildPrintStatementNode(expr)
        return Pair(ParseResult.Success(stmt), p)
    }
}