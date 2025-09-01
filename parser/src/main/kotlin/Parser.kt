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
import type.CommonTypes

class Parser(
    val tokens: List<Token>,
    val nodeBuilder: NodeBuilder,
    val expressionParser: ExpressionParser,
    val statementParser: StatementParser,
    val current: Int = 0,
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
            return accumulatedStatements
        }

        val statementResult = statementParser.parseStatement(currentParser)

        return parseStatementsRecursive(
            statementResult.getParser(),
            accumulatedStatements + statementResult.getStatement(),
        )
    }

    fun peak(): Token? = if (isAtEnd()) null else tokens[current]

    fun advance(): Parser =
        if (!isAtEnd()) {
            Parser(tokens, nodeBuilder, expressionParser, statementParser, current + 1)
        } else {
            this
        }

    fun consume(type: CommonTypes): ParserResult =
        if (checkType(type, peak())) {
            ParserSuccess("Method consume called advance", advance())
        } else {
            ParserError("Can't advance when token is invalid or null", this)
        }

    fun getExpressionParser(): ExpressionParser = expressionParser

    fun getNodeBuilder(): NodeBuilder = nodeBuilder
}
