package parser.command

import Token
import parser.Parser

sealed interface StatementParserCommand : ParserCommand {
    fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean
}
