package parser.command

import node.statement.Statement
import parser.Parser

sealed interface ParserCommand {
    fun parse(parser: Parser): Statement
}
