package parser.command

import node.Statement
import parser.RecursiveDescentParser

sealed interface ParserCommand {
    fun parse(parser: RecursiveDescentParser): Statement
}