package parser.command

import node.Statement
import parser.Parser
import parser.result.ParseResult

sealed interface ParserCommand {
    fun parse(parser: Parser): Pair<ParseResult<Statement>, Parser>
}
