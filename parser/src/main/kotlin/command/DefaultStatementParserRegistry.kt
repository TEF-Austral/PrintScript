package parser.command

import node.statement.Statement
import parser.Parser

class DefaultStatementParserRegistry(private val statementCommands: List<StatementParserCommand>) : ParserCommand {

    override fun parse(parser: Parser): Statement {
        for (command in statementCommands) {
            if (command.canHandle(parser.getCurrentToken(), parser)) {
                return command.parse(parser)
            }
        }
        return parser.getNodeBuilder().buildEmptyStatementNode()
    }
}