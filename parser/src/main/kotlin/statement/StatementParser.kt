package parser.statement

import parser.Parser
import parser.result.StatementErrorResult
import parser.result.StatementResult

class StatementParser(
    private val statementCommands: List<StatementBuilder>,
) {
    fun parse(parser: Parser): StatementResult {
        for (command in statementCommands) {
            if (command.canHandle(parser.peak(), parser)) {
                return command.parse(parser)
            }
        }
        return StatementErrorResult(parser, "Can't be handled currently by the statement parser")
    }
}
