package parser.command

import node.statement.Statement
import parser.Parser
import parser.result.ParseResult

class DefaultStatementParserRegistry(
    private val statementCommands: List<StatementParserCommand>,
) : ParserCommand {
    override fun parse(parser: Parser): Pair<ParseResult<Statement>, Parser> {
        for (command in statementCommands) {
            if (command.canHandle(parser.getCurrentToken(), parser)) {
                return command.parse(parser)
            }
        }
        val empty = parser.getNodeBuilder().buildEmptyStatementNode()
        return Pair(ParseResult.Success(empty), parser)
    }
}
