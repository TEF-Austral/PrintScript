package parser.statement

import node.statement.Statement
import parser.Parser

interface StatementParser {
    fun parseStatement(parser: Parser): Statement
}