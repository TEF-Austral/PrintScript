package parser

import Token
import TokenStream
import builder.NodeBuilder
import node.ASTNode
import node.Statement
import parser.result.CompleteProgram
import parser.result.FailedProgram
import parser.result.FinalResult
import parser.result.ParserError
import parser.result.ParserResult
import parser.result.ParserSuccess
import parser.statement.StatementParser
import parser.statement.expression.ExpressionParsingBuilder
import parser.utils.checkType
import type.CommonTypes

data class Parser(
    private val stream: TokenStream,
    private val nodeBuilder: NodeBuilder,
    private val expressionParser: ExpressionParsingBuilder,
    private val statementParser: StatementParser,
) : ParserInterface {

    fun isAtEnd(): Boolean = stream.isAtEnd()

    override fun parse(): FinalResult =
        try {
            val statements = parseStatements(this, emptyList())
            CompleteProgram(this, nodeBuilder.buildProgramNode(statements))
        } catch (e: Exception) {
            FailedProgram(this, e.message ?: "An unknown error occurred.")
        }

    private fun parseStatements(
        currentParser: Parser,
        accumulatedStatements: List<Statement>,
    ): List<Statement> {
        if (!currentParser.hasNext()) {
            return accumulatedStatements
        }

        val result = currentParser.statementParser.parse(currentParser)
        return when {
            result.isSuccess() -> {
                val nextParser = result.getParser()
                val newStatement = result.getStatement()
                parseStatements(nextParser, accumulatedStatements + newStatement)
            }

            else -> {
                throw Exception(result.message())
            }
        }
    }

    fun advance(): Parser {
        if (isAtEnd()) return this
        val streamResult = stream.next() ?: return this
        return this.copy(stream = streamResult.nextStream)
    }

    fun consume(type: CommonTypes): ParserResult {
        val currentToken = peak()
        return if (currentToken != null && checkType(type, currentToken)) {
            ParserSuccess("Token consumed: $type", advance())
        } else {
            ParserError("Expected $type but found ${currentToken?.getType()}", this)
        }
    }

    override fun hasNext(): Boolean = !isAtEnd()

    override fun next(): ASTNode {
        if (!hasNext()) {
            throw NoSuchElementException("No more elements to parse.")
        }
        val result = statementParser.parse(this)

        if (result.isSuccess()) {
            return result.getStatement()
        } else {
            throw IllegalStateException("Failed to parse the next node: ${result.message()}")
        }
    }

    override fun peak(): Token? = stream.peak()

    override fun getExpressionParser(): ExpressionParsingBuilder = expressionParser

    override fun getNodeBuilder(): NodeBuilder = nodeBuilder

    override fun getStatementParser(): StatementParser = statementParser
}
