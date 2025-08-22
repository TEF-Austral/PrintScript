package parser.command

import parser.result.ParseResult
import parser.Parser
import node.statement.Statement

class DefaultStatementParserRegistry(
    private val statementCommands: List<StatementParserCommand>
) : ParserCommand {

    override fun parse(parser: Parser): ParseResult<Statement> {
        for (command in statementCommands) {
            if (command.canHandle(parser.getCurrentToken(), parser)) {
                return command.parse(parser)
            }
        }
        return ParseResult.Success(parser.getNodeBuilder().buildEmptyStatementNode())
    }
}