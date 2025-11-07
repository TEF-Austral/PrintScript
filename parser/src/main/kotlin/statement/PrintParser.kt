package parser.statement

import Token
import coordinates.Position
import parser.Parser
import parser.exception.ParserException
import parser.result.StatementBuiltResult
import parser.result.StatementResult
import parser.utils.advancePastSemiColon
import parser.utils.isClosingParenthesis
import parser.utils.isOpeningParenthesis
import type.CommonTypes

class PrintParser : StatementBuilder {
    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean = token?.getType() == CommonTypes.PRINT

    override fun parse(parser: Parser): StatementResult {
        val printParser = parser.consume(CommonTypes.PRINT).getParser()
        val afterOpeningParenthesisParser = openingParenthesisConsumption(printParser)
        val expression =
            afterOpeningParenthesisParser.getExpressionParser().parseExpression(
                afterOpeningParenthesisParser,
            )
        val beforeClosingParenthesisParser =
            closingParenthesisConsumption(expression.getParser(), afterOpeningParenthesisParser)
        val finalDelimiterParser =
            consumeSemiColon(beforeClosingParenthesisParser, expression.getParser())
        val builtStatement =
            finalDelimiterParser.getNodeBuilder().buildPrintStatementNode(
                expression.getExpression(),
            )
        return StatementBuiltResult(finalDelimiterParser, builtStatement)
    }

    private fun openingParenthesisConsumption(parser: Parser): Parser {
        if (isOpeningParenthesis(parser)) {
            return parser.consume(CommonTypes.DELIMITERS).getParser()
        }
        val token = parser.peak()
        val coordinates = token?.getCoordinates()
        throw ParserException("Was expecting opening parenthesis", coordinates)
    }

    private fun closingParenthesisConsumption(
        parser: Parser,
        previousParser: Parser,
    ): Parser {
        if (isClosingParenthesis(parser)) {
            return parser.consume(CommonTypes.DELIMITERS).getParser()
        }
        val token = previousParser.peak()
        val coordinates = token?.getCoordinates()!!
        throw ParserException(
            "Was expecting closing parenthesis",
            Position(
                coordinates.getRow(),
                coordinates.getColumn() + 1,
            ),
        )
    }

    private fun consumeSemiColon(
        parser: Parser,
        previousParser: Parser,
    ): Parser {
        val nextParser = advancePastSemiColon(parser)
        if (nextParser == parser) {
            val token = previousParser.peak()
            val coordinates = token?.getCoordinates()!!
            throw ParserException(
                "Was expecting a semicolon",
                Position(
                    coordinates.getRow(),
                    coordinates.getColumn() + 1,
                ),
            )
        }
        return nextParser
    }
}
