package parser.command

import parser.Parser
import parser.result.StatementBuiltResult
import parser.result.StatementResult

class DefaultStatementParserRegistry(
    private val statementCommands: List<StatementParserCommand>,
) : ParserCommand {
    override fun parse(parser: Parser): StatementResult {
        for (command in statementCommands) {
            if (command.canHandle(parser.peak(), parser)) {
                return command.parse(parser)
            }
        }
        return StatementBuiltResult(parser, parser.getNodeBuilder().buildEmptyStatementNode())
    }
}
