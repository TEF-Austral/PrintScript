package parser.statement

import Token
import parser.Parser
import parser.result.StatementBuiltResult
import parser.result.StatementResult
import parser.utils.isClosingParenthesis
import parser.utils.isOpeningParenthesis
import parser.utils.isSemiColon
import type.CommonTypes

class PrintParser : StatementBuilder {
    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean = token?.getType() == CommonTypes.PRINT

    override fun parse(parser: Parser): StatementResult {
        val printParser = parser.consume(CommonTypes.PRINT).getParser()
        val beforeOpeningParenthesisParser = openingParenthesisConsumption(printParser)
        val expression = beforeOpeningParenthesisParser.getExpressionParser().parseExpression(parser)
        val beforeClosingParenthesisParser = closingParenthesisConsumption(beforeOpeningParenthesisParser)
        val finalDelimiterParser = consumeSemiColon(beforeClosingParenthesisParser)
        val builtStatement = finalDelimiterParser.getNodeBuilder().buildPrintStatementNode(expression.getExpression())
        return StatementBuiltResult(finalDelimiterParser, builtStatement)
    }

    private fun openingParenthesisConsumption(parser: Parser): Parser {
        if (isOpeningParenthesis(parser)) {
            return parser.consume(CommonTypes.DELIMITERS).getParser()
        }
        throw Exception("What is printed should be enclose by parenthesis -> (")
    }

    private fun closingParenthesisConsumption(parser: Parser): Parser {
        if (isClosingParenthesis(parser)) {
            return parser.consume(CommonTypes.DELIMITERS).getParser()
        }
        throw Exception("What is printed should be enclose by parenthesis -> (")
    }

    private fun consumeSemiColon(parser: Parser): Parser {
        if (isSemiColon(parser.peak())) {
            return parser.consume(CommonTypes.DELIMITERS).getParser()
        }
        throw Exception("Was expecting a semicolon")
    }

}
