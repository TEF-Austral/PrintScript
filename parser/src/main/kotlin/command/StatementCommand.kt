package parser.command

import Token
import parser.Parser

sealed interface StatementCommand : ParserCommand {
    fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean
}
