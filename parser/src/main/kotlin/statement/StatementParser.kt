package parser.statement

import parser.Parser
import parser.command.AssignmentParser
import parser.command.DefaultStatementParserRegistry
import parser.command.ExpressionParser
import parser.command.ParserCommand
import parser.command.PrintParser
import parser.command.StatementCommand
import parser.command.VariableDeclarationParser
import parser.result.StatementResult

class StatementParser(
    private val listOfCommands: List<StatementCommand> =
        listOf(
            VariableDeclarationParser(),
            AssignmentParser(),
            PrintParser(),
            ExpressionParser(),
        ),
    private val registry: ParserCommand = DefaultStatementParserRegistry(listOfCommands),
) {
    fun parseStatement(parser: Parser): StatementResult = registry.parse(parser)
}
