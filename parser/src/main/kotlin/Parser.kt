package parser

import Token
import builder.NodeBuilder
import node.EmptyStatement
import node.Statement
import parser.statement.expression.ExpressionParsingBuilder
import parser.result.CompleteProgram
import parser.result.FailedProgram
import parser.result.FinalResult
import parser.result.ParserError
import parser.result.ParserResult
import parser.result.ParserSuccess
import parser.statement.StatementParser
import parser.result.StatementBuiltResult
import parser.result.StatementResult
import parser.utils.checkType
import type.CommonTypes

class Parser(
    private val tokens: List<Token>,
    private val nodeBuilder: NodeBuilder,
    private val expressionParser: ExpressionParsingBuilder,
    private val statementParser: StatementParser,
    private val current: Int = 0
) {
    fun isAtEnd(): Boolean = current >= tokens.size

    fun parse(): FinalResult =
        try {
            val statements = parseStatementsRecursive(StatementBuiltResult(this, EmptyStatement()), emptyList())
            CompleteProgram(this, nodeBuilder.buildProgramNode(statements))
        } catch (e: Exception) {
            FailedProgram(this, e.message.toString())
        }

    private fun parseStatementsRecursive(result: StatementResult, accumulatedStatements: List<Statement>): List<Statement> {
        if (result.getParser().isAtEnd()) {
            return accumulatedStatements
        }
        if (!result.isSuccess()){
            throw Exception(result.message()) //TODO see if it is possible to combine errors.
        }
        val statementResult = statementParser.parse(result.getParser())

        return parseStatementsRecursive(statementResult, accumulatedStatements + statementResult.getStatement())
    }

    fun peak(): Token? = if (isAtEnd()) null else tokens[current]

    fun advance(): Parser {
        if (isAtEnd()) return this
        return Parser(
            tokens,
            nodeBuilder,
            expressionParser,
            statementParser,
            current + 1
        )
    }

    fun consume(type: CommonTypes): ParserResult =
        if (checkType(type, peak())) {
            ParserSuccess("Method consume called advance", advance())
        } else {
            ParserError("Can't advance when token is invalid or null", this)
        }

    fun getExpressionParser(): ExpressionParsingBuilder = expressionParser

    fun getNodeBuilder(): NodeBuilder = nodeBuilder

    fun getStatementParser(): StatementParser = statementParser

    fun getCurrent(): Int = current

    fun getTokens(): List<Token> = tokens
}
