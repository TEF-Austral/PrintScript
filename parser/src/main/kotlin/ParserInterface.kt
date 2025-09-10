package parser

import Token
import builder.NodeBuilder
import parser.result.FinalResult
import parser.statement.StatementParser
import parser.statement.expression.ExpressionParsingBuilder

interface ParserInterface {
    fun parse(): FinalResult

    fun getExpressionParser(): ExpressionParsingBuilder

    fun getNodeBuilder(): NodeBuilder

    fun getStatementParser(): StatementParser

    fun getCurrent(): Int

    fun getTokens(): List<Token>
}
