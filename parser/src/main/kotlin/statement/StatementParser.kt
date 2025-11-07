package parser.statement

import parser.Parser
import parser.exception.ParserException
import parser.result.StatementResult

class StatementParser(
    private val statementCommands: List<StatementBuilder>,
) {
    fun parse(parser: Parser): StatementResult {
        for (command in statementCommands) {
            val ast = parser.peak()
            if (command.canHandle(ast, parser)) {
                val result = command.parse(parser)
                if (!result.isSuccess()) {
                    continue
                }
                return result
            }
        }
        throw ParserException(
            "Couldn't handle statement, statement can't start with ${parser.peak()?.getValue()}",
            parser.peak()?.getCoordinates(),
        )
    }
}
