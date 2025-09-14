package parser

import Token
import TokenStream
import builder.NodeBuilder
import node.EmptyExpression
import node.Statement
import parser.result.CompleteProgram
import parser.result.FailedProgram
import parser.result.FinalResult
import parser.result.NextResult
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

    override fun isAtEnd(): Boolean = stream.isAtEnd()

    override fun parse(): FinalResult =
        try {
            val statements = parseStatements(this, emptyList())
            CompleteProgram(this, nodeBuilder.buildProgramNode(statements))
        } catch (e: Exception) {
            // DEBUG: Captura cualquier excepción que detenga el parseo
            FailedProgram(this, e.message ?: "An unknown error occurred.")
        }

    private fun parseStatements(
        currentParser: Parser,
        accumulatedStatements: List<Statement>,
    ): List<Statement> {
        val statementNum = accumulatedStatements.size + 1

        // Cambia la condición para procesar el último token
        if (currentParser.peak() == null) {
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
                throw Exception("Failed at statement #$statementNum: ${result.message()}")
            }
        }
    }

    fun advance(): Parser {
        if (isAtEnd()) {
            return this
        }
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

    override fun next(): NextResult {
        try {
            if (!hasNext()) {
                return NextResult(EmptyExpression(), false, "No more tokens available", this)
            }

            val result = statementParser.parse(this)

            return if (result.isSuccess()) {
                val nextParser = result.getParser()
                NextResult(result.getStatement(), true, "Parsed next node successfully", nextParser)
            } else {
                NextResult(
                    EmptyExpression(),
                    false,
                    "\"Failed to parse the next node: ${result.message()}\"",
                    this,
                )
            }
        } catch (e: Exception) {
            return NextResult(
                EmptyExpression(),
                false,
                "\"Failed to parse the next node: ${e.message}\"",
                this,
            )
        }
    }

    override fun peak(): Token? = stream.peak()

    override fun getExpressionParser(): ExpressionParsingBuilder = expressionParser

    override fun getNodeBuilder(): NodeBuilder = nodeBuilder

    override fun getStatementParser(): StatementParser = statementParser
}
