package parser.factory

import Token
import builder.NodeBuilder
import parser.command.StatementParser
import parser.Parser
import parser.command.AssignmentParser
import parser.command.ExpressionParser
import parser.command.ParenthesisParser
import parser.command.PrintParser
import parser.command.StatementCommand
import parser.command.VariableDeclarationParser
import parser.expression.ExpressionParsingBuilder
import parser.expression.binary.DefaultParseBinary
import parser.expression.DelimitersBuilder
import parser.expression.ExpressionRegistry
import parser.expression.IdentifierBuilder
import parser.expression.LiteralBuilder

class RecursiveParserFactory : ParserFactory {
    override fun createParser(
        tokens: List<Token>,
        nodeBuilder: NodeBuilder,
    ): Parser {
        val tokenToExpression =
            ExpressionRegistry(
                listOf(
                    IdentifierBuilder,
                    LiteralBuilder,
                    DelimitersBuilder,
                ),
            )
        val parseBinary =
            DefaultParseBinary(
                ExpressionRegistry(
                    listOf(
                        IdentifierBuilder,
                        LiteralBuilder,
                        DelimitersBuilder,
                    ),
                ),
            )
        val expressionParser = ExpressionParsingBuilder(tokenToExpression, parseBinary)
        val permittedStatementsForParenthesis: List<StatementCommand> = listOf(
            AssignmentParser(),
            PrintParser(),
            ExpressionParser())
        val statementParser = StatementParser(listOf(
            VariableDeclarationParser(),
            AssignmentParser(),
            PrintParser(),
            ExpressionParser(),
            ParenthesisParser(permittedStatementsForParenthesis)
        ))
        return Parser(tokens, nodeBuilder, expressionParser, statementParser)
    }

    override fun withNewTokens(
        tokens: List<Token>,
        parser: Parser,
    ): Parser = Parser(tokens, parser.getNodeBuilder(), parser.getExpressionParser(), parser.getStatementParser(), parser.getCurrent())
}
