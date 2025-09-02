package parser.command

import Token
import parser.Parser
import parser.result.StatementBuiltResult
import parser.result.StatementResult
import type.CommonTypes

class PrintParser : StatementCommand {
    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean = token?.getType() == CommonTypes.PRINT

    override fun parse(parser: Parser): StatementResult {
        val printParser = parser.consume(CommonTypes.PRINT).getParser()
        val delimiterParser = printParser.consume(CommonTypes.DELIMITERS).getParser() // (
        val expression = delimiterParser.getExpressionParser().parseExpression(parser)
        val nextDelimiterParser = delimiterParser.consume(CommonTypes.DELIMITERS).getParser() // )
        val finalDelimiterParser = nextDelimiterParser.consume(CommonTypes.DELIMITERS).getParser() // ;
        val builtStatement = finalDelimiterParser.getNodeBuilder().buildPrintStatementNode(expression.getExpression())
        return StatementBuiltResult(finalDelimiterParser, builtStatement)
    }
    // TODO: Soporte de múltiples Delimiters, porque aca debria de haber o quisa no un
}
