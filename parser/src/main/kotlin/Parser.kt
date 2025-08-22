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

class Parser(
    private val tokens: List<Token>,
    private val nodeBuilder: NodeBuilder,
    private val expressionParser: ExpressionParser,
    private val statementParser: StatementParser
) {
    var current = 0

    fun parse(): ParseResult<Program> {
        val statements = mutableListOf<Statement>()

        while (!isAtEnd()) {
            when (val stmtRes = statementParser.parseStatement(this)) {
                is ParseResult.Failure -> return ParseResult.Failure(stmtRes.error)
                is ParseResult.Success -> statements.add(stmtRes.value)
            }
        }

        return ParseResult.Success(nodeBuilder.buildProgramNode(statements))
    }

    fun getCurrentToken(): Token? = if (isAtEnd()) null else tokens[current]

    fun advance(): Token? {
        if (!isAtEnd()) current++
        return previous()
    }

    fun previous(): Token? = if (current == 0) null else tokens[current - 1]

    fun isAtEnd(): Boolean = current >= tokens.size

    fun check(type: TokenType): Boolean = if (isAtEnd()) false else getCurrentToken()?.getType() == type

    fun match(vararg types: TokenType): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    fun consumeOrError(expected: TokenType): ParseResult<Token> {
        val current = getCurrentToken()
        val pos = current?.getCoordinates() ?: Position(-1, -1)
        val got = current?.getType()?.toString() ?: "end-of-input"
        return if (current?.getType() == expected) {
            advance()
            ParseResult.Success(current)
        } else {
            ParseResult.Failure(ParseError("Expected $expected but found $got", pos))
        }
    }

    fun getExpressionParser(): ExpressionParser = expressionParser
    fun getNodeBuilder(): NodeBuilder = nodeBuilder
}