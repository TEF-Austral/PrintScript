package parser.command

import node.Statement
import parser.RecursiveDescentParser

class DefaultStatementParserRegistry(private val statementCommands: List<StatementParserCommand>) : StatementParserCommand {

    override fun parse(parser: RecursiveDescentParser): Statement {
        for (command in statementCommands) {
            if (command.canHandle()) {
                return command.parse(parser)
            }
        }
    }

}





