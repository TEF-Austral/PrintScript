package parser.command

import parser.result.ParseResult
import parser.Parser
import node.statement.Statement

class DefaultStatementParserRegistry(
    private val statementCommands: List<StatementParserCommand>
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