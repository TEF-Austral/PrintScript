package parser.command

import node.Statement
import parser.RecursiveDescentParser

class DefaultStatementParserRegistry(private val statementCommands: List<StatementParserCommand>) : ParserCommand {

    override fun parse(parser: RecursiveDescentParser): Statement {
        for (command in statementCommands) {
            if (command.canHandle(parser.getCurrentToken(),parser)) {
                return command.parse(parser)
            }
        }

    }

}





