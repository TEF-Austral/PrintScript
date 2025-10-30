package parser.statement

import parser.Parser
import parser.result.StatementErrorResult
import parser.result.StatementResult

class StatementParser(
    private val statementCommands: List<StatementBuilder>,
) {
    fun parse(parser: Parser): StatementResult {
        var error = ""
        for (command in statementCommands) {
            val ast = parser.peak()
            if (command.canHandle(ast, parser)) {
                val result = command.parse(parser)
                if (!result.isSuccess()) {
                    error += result.message() + " "
                    continue
                }
                return result
            }
        }

        if (error.isEmpty()) {
            val token = parser.peak()!!
            return StatementErrorResult(
                parser,
                "Invalid structure, can't parse. Found: ${token.getValue()} in " +
                    token.getCoordinates().getRow() +
                    ":" +
                    token.getCoordinates().getColumn(),
            )
        }
        return StatementErrorResult(parser, "Couldn't handle, none of the following passed: $error")
    }
}
