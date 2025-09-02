package parser.command

import parser.Parser
import parser.result.StatementErrorResult
import parser.result.StatementResult

class DefaultStatementParserRegistry(
    private val statementCommands: List<StatementCommand>,
) : ParserCommand {
    override fun parse(parser: Parser): StatementResult {
        for (command in statementCommands) {
            if (command.canHandle(parser.peak(), parser)) {
                return command.parse(parser)
            }
        }
        return StatementErrorResult(parser, "Unhandleable token")
    }
}
