package parser.statement

import node.statement.Statement
import parser.Parser
import parser.command.DefaultStatementParserRegistry
import parser.command.StatementParserCommand
import parser.result.ParseResult

class StatementParser {

    private val registry: DefaultStatementParserRegistry

    init {
        val commands = createStatementCommands()
        registry = DefaultStatementParserRegistry(commands)
    }

    fun parseStatement(parser: Parser): ParseResult<Statement> {
        return registry.parse(parser)
    }

    private fun createStatementCommands(): List<StatementParserCommand> {
        return listOf(
            parser.command.VariableDeclarationParser(),
            parser.command.AssignmentParser(),
            parser.command.PrintStatementParser(),
            parser.command.ExpressionStatementParser()
        )
    }
}
