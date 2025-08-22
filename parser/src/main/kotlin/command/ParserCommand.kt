package parser.command

import node.statement.Statement
import parser.Parser
import parser.result.ParseResult

sealed interface ParserCommand {
    fun parse(parser: Parser): ParseResult<Statement>
}