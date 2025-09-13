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

    override fun parse(): FinalResult {
        // DEBUG: Indica el inicio del proceso de parseo
        println("[DEBUG] ==> Starting main parse...")
        return try {
            val statements = parseStatements(this, emptyList())
            // DEBUG: Muestra el resultado final del parseo
            println("[DEBUG] ==> Finished parsing. Total statements parsed: ${statements.size}")
            CompleteProgram(this, nodeBuilder.buildProgramNode(statements))
        } catch (e: Exception) {
            // DEBUG: Captura cualquier excepción que detenga el parseo
            println("[DEBUG] ==> Main parse failed with exception: ${e.message}")
            FailedProgram(this, e.message ?: "An unknown error occurred.")
        }
    }

    private fun parseStatements(
        currentParser: Parser,
        accumulatedStatements: List<Statement>,
    ): List<Statement> {
        val statementNum = accumulatedStatements.size + 1
        println(
            "[DEBUG] parseStatements: Statement #$statementNum, Token -> ${currentParser.peak()}",
        )

        // Cambia la condición para procesar el último token
        if (currentParser.peak() == null) {
            println(
                "[DEBUG] parseStatements: Reached end of stream. Finalizing. All statements parsed successfully.",
            )
            println("[DEBUG] Last token before end: ${currentParser.peak()}")
            return accumulatedStatements
        }

        val result = currentParser.statementParser.parse(currentParser)
        return when {
            result.isSuccess() -> {
                val nextParser = result.getParser()
                val newStatement = result.getStatement()
                println(
                    "[DEBUG] parseStatements: Successfully parsed statement #$statementNum -> $newStatement",
                )
                parseStatements(nextParser, accumulatedStatements + newStatement)
            }

            else -> {
                println(
                    "[DEBUG] parseStatements: FAILED at statement #$statementNum. Error: ${result.message()}",
                )
                throw Exception("Failed at statement #$statementNum: ${result.message()}")
            }
        }
    }

    fun advance(): Parser {
        if (isAtEnd()) {
            println("[DEBUG] advance(): Stream is at end, cannot advance further.")
            return this
        }
        val streamResult = stream.next()
        if (streamResult == null) {
            println("[DEBUG] advance(): stream.next() returned null, no more tokens.")
            return this
        }
        // DEBUG: Anuncia que el stream avanza al siguiente token
        println("[DEBUG] Advancing stream. Next token will be: ${streamResult.nextStream.peak()}")
        return this.copy(stream = streamResult.nextStream)
    }

    fun consume(type: CommonTypes): ParserResult {
        val currentToken = peak()
        // DEBUG: Muestra el intento de consumir un token específico
        println(
            "[DEBUG] Attempting to consume token of type '$type'. Current token is: $currentToken",
        )
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
