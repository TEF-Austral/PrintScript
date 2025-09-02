package parser.command

import Token
import parser.Parser
import parser.result.StatementResult

sealed interface StatementCommand {
    fun parse(parser: Parser): StatementResult

    fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean
}
