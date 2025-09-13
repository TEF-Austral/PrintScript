package parser

import Token
import builder.NodeBuilder
import node.ASTNode
import parser.result.FinalResult
import parser.statement.StatementParser
import parser.statement.expression.ExpressionParsingBuilder

interface ParserInterface {
    fun parse(): FinalResult

    fun getExpressionParser(): ExpressionParsingBuilder

    fun getNodeBuilder(): NodeBuilder

    fun getStatementParser(): StatementParser

    fun hasNext(): Boolean

    fun next(): ASTNode

    fun peak(): Token?
}
