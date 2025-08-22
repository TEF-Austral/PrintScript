package parser.command

import Token
import parser.result.ParseResult
import parser.Parser
import node.statement.Statement

interface StatementParserCommand {
    fun canHandle(token: Token?, parser: Parser): Boolean
    fun parse(parser: Parser): ParseResult<Statement>
}