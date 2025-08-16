package parser.statement

import node.Statement
import parser.RecursiveDescentParser

interface StatementParser {
    fun parseStatement(parser: RecursiveDescentParser): Statement
}