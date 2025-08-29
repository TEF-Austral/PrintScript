package parser.command

import Token
import node.Statement
import parser.Parser
import parser.result.ParseResult

interface StatementParserCommand {
    fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean

    fun parse(parser: Parser): Pair<ParseResult<Statement>, Parser>
}
