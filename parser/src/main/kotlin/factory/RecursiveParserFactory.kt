package parser.factory

import Token
import builder.NodeBuilder
import parser.command.StatementParser
import parser.Parser
import parser.expression.ExpressionParser
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
        val expressionParser = ExpressionParser(tokenToExpression, parseBinary)
        val statementParser = StatementParser()
        return Parser(tokens, nodeBuilder, expressionParser, statementParser, 0)
    }

    override fun withNewTokens(
        tokens: List<Token>,
        parser: Parser,
    ): Parser = Parser(tokens, parser.getNodeBuilder(), parser.getExpressionParser(), parser.getStatementParser(), parser.getCurrent())
}
