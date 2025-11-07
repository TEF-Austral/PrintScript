package parser.statement

import Token
import node.Statement
import parser.Parser
import parser.exception.ParserException
import parser.result.StatementBuiltResult
import parser.result.StatementResult
import parser.utils.checkType
import parser.utils.isClosingBrace
import parser.utils.isOpeningParenthesis
import parser.utils.isClosingParenthesis
import parser.utils.isOpeningBrace
import type.CommonTypes

class IfStatement : StatementBuilder {
    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean = checkType(CommonTypes.CONDITIONALS, token) && token?.getValue() == "if"

    override fun parse(parser: Parser): StatementResult {
        val afterIf = consumeIfKeyword(parser)
        val afterOpenParen = consumeOpeningParenthesis(afterIf)
        val conditionResult = parseCondition(afterOpenParen)
        val afterCloseParen = consumeClosingParenthesis(conditionResult.getParser())
        val (afterThenStatement, thenStatement) = parseBlockWithBraces(afterCloseParen, "then")
        val (finalParser, elseStatement) = parseElseClause(afterThenStatement)

        val ifStatement =
            finalParser.getNodeBuilder().buildIfStatementNode(
                conditionResult.getExpression(),
                thenStatement,
                elseStatement,
            )

        return StatementBuiltResult(finalParser, ifStatement)
    }

    private fun consumeIfKeyword(parser: Parser): Parser {
        val ifKeyword = parser.consume(CommonTypes.CONDITIONALS)
        if (!ifKeyword.isSuccess()) {
            val coordinates = parser.peak()!!.getCoordinates()
            throw ParserException("Expected 'if' keyword", coordinates)
        }
        return ifKeyword.getParser()
    }

    private fun consumeOpeningParenthesis(parser: Parser): Parser {
        if (!isOpeningParenthesis(parser)) {
            val coordinates = parser.peak()!!.getCoordinates()
            throw ParserException("Expected '(' after 'if'", coordinates)
        }
        return parser.advance()
    }

    private fun parseCondition(parser: Parser): parser.result.ExpressionResult {
        val conditionResult = parser.getExpressionParser().parseExpression(parser)
        if (!conditionResult.isSuccess()) {
            val coordinates = parser.peak()!!.getCoordinates()
            throw ParserException(
                "Expected condition expression: ${conditionResult.message()}",
                coordinates,
            )
        }
        return conditionResult
    }

    private fun consumeClosingParenthesis(parser: Parser): Parser {
        if (!isClosingParenthesis(parser)) {
            val coordinates = parser.peak()!!.getCoordinates()
            throw ParserException("Expected ')' after condition", coordinates)
        }
        return parser.advance()
    }

    private fun parseElseClause(parser: Parser): Pair<Parser, Statement?> {
        if (parser.hasNext() &&
            checkType(CommonTypes.CONDITIONALS, parser.peak()) &&
            parser.peak()?.getValue() == "else"
        ) {
            val afterElse = parser.advance()

            return if (isOpeningBrace(afterElse)) {
                parseBlockWithBraces(afterElse, "else")
            } else {
                val elseResult = afterElse.getStatementParser().parse(afterElse)
                if (!elseResult.isSuccess()) {
                    val coordinates = afterElse.peak()!!.getCoordinates()
                    throw ParserException(
                        "Error parsing else statement: ${elseResult.message()}",
                        coordinates,
                    )
                }
                Pair(elseResult.getParser(), elseResult.getStatement())
            }
        } else {
            return Pair(parser, null)
        }
    }

    private fun parseBlockWithBraces(
        parser: Parser,
        blockType: String,
    ): Pair<Parser, Statement> {
        val afterOpenBrace = consumeOpeningBrace(parser, blockType)
        val statementResult = parseBlockStatement(afterOpenBrace, blockType)
        val afterCloseBrace = consumeClosingBrace(statementResult.getParser(), blockType)
        return Pair(afterCloseBrace, statementResult.getStatement())
    }

    private fun consumeOpeningBrace(
        parser: Parser,
        blockType: String,
    ): Parser {
        if (!isOpeningBrace(parser)) {
            val coordinates = parser.peak()!!.getCoordinates()
            throw ParserException("Expected '{' to start $blockType block", coordinates)
        }
        return parser.advance()
    }

    private fun parseBlockStatement(
        parser: Parser,
        blockType: String,
    ): StatementResult {
        val statementResult = parser.getStatementParser().parse(parser)
        if (!statementResult.isSuccess()) {
            val coordinates = parser.peak()!!.getCoordinates()
            throw ParserException(
                "Error parsing $blockType statement: ${statementResult.message()}",
                coordinates,
            )
        }
        return statementResult
    }

    private fun consumeClosingBrace(
        parser: Parser,
        blockType: String,
    ): Parser {
        if (!isClosingBrace(parser)) {
            val coordinates = parser.peak()!!.getCoordinates()
            throw ParserException("Expected '}' to close $blockType block", coordinates)
        }
        return parser.advance()
    }
}
