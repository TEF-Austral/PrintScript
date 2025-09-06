package parser.factory

import Token
import builder.NodeBuilder
import parser.statement.StatementParser
import parser.Parser
import parser.ParserInterface
import parser.statement.AssignmentParser
import parser.statement.ExpressionParser
import parser.statement.PrintParser
import parser.statement.VariableDeclarationParser
import parser.statement.expression.ExpressionParsingBuilder
import parser.statement.binary.DefaultParseBinary
import parser.statement.expression.DelimitersBuilder
import parser.statement.expression.ExpressionRegistry
import parser.statement.expression.IdentifierBuilder
import parser.statement.expression.LiteralBuilder

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
        val statementParser =
            StatementParser(
                listOf(
                    VariableDeclarationParser(),
                    PrintParser(),
                    AssignmentParser(),
                    ExpressionParser(),
                ),
            )
        return Parser(tokens, nodeBuilder, expressionParser, statementParser)
    }

    override fun withNewTokens(
        tokens: List<Token>,
        parser: ParserInterface,
    ): Parser = Parser(tokens, parser.getNodeBuilder(), parser.getExpressionParser(), parser.getStatementParser(), parser.getCurrent())
}
