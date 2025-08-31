package parser.command

import parser.Parser
import parser.result.StatementResult

sealed interface ParserCommand {
    fun parse(parser: Parser): StatementResult
}
