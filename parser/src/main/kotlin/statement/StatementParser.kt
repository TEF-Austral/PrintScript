package parser.statement

import parser.Parser
import parser.command.DefaultStatementParserRegistry
import parser.result.StatementResult

class StatementParser(
    private val registry: DefaultStatementParserRegistry =
        DefaultStatementParserRegistry(
            listOf(
                parser.command.VariableDeclarationParser(),
                parser.command.AssignmentParser(),
                parser.command.PrintStatementParser(),
                parser.command.ExpressionStatementParser(),
            ),
        ),
) {
    fun parseStatement(parser: Parser): StatementResult = registry.parse(parser)
}
