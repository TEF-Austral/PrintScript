package parser

import Position
import Token
import TokenType
import builder.NodeBuilder
import node.Program
import node.statement.Statement
import parser.expression.ExpressionParser
import parser.result.ParseError
import parser.result.ParseResult
import parser.statement.StatementParser

data class Parser(
    private val tokens: List<Token>,
    private val nodeBuilder: NodeBuilder,
    private val expressionParser: ExpressionParser,
    private val statementParser: StatementParser,
    val current: Int = 0
) {
    fun isAtEnd(): Boolean = current >= tokens.size

    fun getCurrentToken(): Token? = tokens.getOrNull(current)

    fun previous(): Token? = tokens.getOrNull(current - 1)

    // returns a new Parser with current + 1
    fun advance(): Parser =
        copy(current = if (!isAtEnd()) current + 1 else current)

    // returns the consumed token (if any) and the new Parser
    fun consumeOrError(expected: TokenType): Pair<ParseResult<Token>, Parser> {
        val tok = getCurrentToken()
        val pos = tok?.getCoordinates() ?: Position(-1, -1)
        return if (tok?.getType() == expected) {
            Pair(ParseResult.Success(tok), advance())
        } else {
            val got = tok?.getType()?.toString() ?: "end-of-input"
            Pair(ParseResult.Failure(ParseError("Expected $expected but found $got", pos)), this)
        }
    }

    fun parse(): Pair<ParseResult<Program>, Parser> {
        var p = this
        val statements = mutableListOf<Statement>()
        while (!p.isAtEnd()) {
            val (stmtRes, nextParser) = statementParser.parseStatement(p)
            when (stmtRes) {
                is ParseResult.Success -> {
                    statements += stmtRes.value
                    p = nextParser
                }
                is ParseResult.Failure -> return Pair(ParseResult.Failure(stmtRes.error), nextParser)
            }
        }
        return Pair(ParseResult.Success(nodeBuilder.buildProgramNode(statements)), p)
    }

    fun getExpressionParser(): ExpressionParser = expressionParser
    fun getNodeBuilder(): NodeBuilder = nodeBuilder
}