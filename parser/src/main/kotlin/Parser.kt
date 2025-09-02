package parser

import Token
import builder.NodeBuilder
import node.Statement
import parser.expression.ExpressionParser
import parser.result.CompleteProgram
import parser.result.FailedProgram
import parser.result.FinalResult
import parser.result.ParserError
import parser.result.ParserResult
import parser.result.ParserSuccess
import parser.statement.StatementParser
import parser.utils.checkType
import parser.utils.DelimiterStack
import type.CommonTypes

class Parser(
    private val tokens: List<Token>,
    private val nodeBuilder: NodeBuilder,
    private val expressionParser: ExpressionParser,
    private val statementParser: StatementParser,
    private val current: Int = 0,
    private val delimiterStack: DelimiterStack = DelimiterStack(),
) {
    fun isAtEnd(): Boolean = current >= tokens.size

    fun parse(): FinalResult =
        try {
            val statements = parseStatementsRecursive(this, emptyList())
            CompleteProgram(this, nodeBuilder.buildProgramNode(statements))
        } catch (e: Exception) {
            FailedProgram(this, e.message.toString())
        }

    private fun parseStatementsRecursive(
        currentParser: Parser,
        accumulatedStatements: List<Statement>,
    ): List<Statement> {
        if (currentParser.isAtEnd()) {
            checkDelimiterStack(currentParser)
            return accumulatedStatements
        }

        val statementResult = statementParser.parseStatement(currentParser)

        return parseStatementsRecursive(
            statementResult.getParser(),
            accumulatedStatements + statementResult.getStatement(),
        )
    }

    private fun checkDelimiterStack(currentParser: Parser) {
        if (!currentParser.delimiterStack.isValidDelimiterState()) {
            throw Exception(delimiterStack.getValidationError() ?: "Invalid delimiter state")
        }
    }

    fun peak(): Token? = if (isAtEnd()) null else tokens[current]

    fun advance(): Parser {
        if (isAtEnd()) return this

        val currentToken = tokens[current]
        val newDelimiterStack =
            if (currentToken.getType() == CommonTypes.DELIMITERS) {
                delimiterStack.add(currentToken)
            } else {
                delimiterStack
            }

        return Parser(
            tokens,
            nodeBuilder,
            expressionParser,
            statementParser,
            current + 1,
            newDelimiterStack,
        )
    }

    fun consume(type: CommonTypes): ParserResult =
        if (checkType(type, peak())) {
            ParserSuccess("Method consume called advance", advance())
        } else {
            ParserError("Can't advance when token is invalid or null", this)
        }

    fun getExpressionParser(): ExpressionParser = expressionParser

    fun getNodeBuilder(): NodeBuilder = nodeBuilder

    fun getStatementParser(): StatementParser = statementParser

    fun getCurrent(): Int = current

    fun getTokens(): List<Token> = tokens
}
