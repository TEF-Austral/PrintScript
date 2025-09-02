package parser.command

import parser.Parser
import parser.result.StatementErrorResult
import parser.result.StatementResult

class StatementParser(
    private val statementCommands: List<StatementCommand> =
        listOf(
            VariableDeclarationParser(),
            AssignmentParser(),
            PrintParser(),
            ExpressionParser(),
        ),
) {
    fun parse(parser: Parser): StatementResult {
        for (command in statementCommands) {
            if (command.canHandle(parser.peak(), parser)) {
                return command.parse(parser)
            }
        }
        return StatementErrorResult(parser, "Unhandleable token")
    }
}
