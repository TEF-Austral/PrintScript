package parser.factory

import Token
import builder.NodeBuilder
import parser.Parser
import parser.ParserInterface
import parser.statement.StatementParser
import parser.statement.expression.ExpressionParsingBuilder
import type.Version

class DefaultParserFactory : ParserFactory {
    override fun createWithVersion(
        version: Version,
        nodeBuilder: NodeBuilder,
        tokenList: List<Token>,
    ): ParserInterface =
        when (version) {
            Version.VERSION_1_0 -> VOnePointZeroParserFactory().createParser(tokenList, nodeBuilder)
            Version.VERSION_1_1 -> VOnePointOneParserFactory().createParser(tokenList, nodeBuilder)
        }

    override fun createDefault(
        nodeBuilder: NodeBuilder,
        tokenList: List<Token>,
    ): ParserInterface = createWithVersion(Version.VERSION_1_1, nodeBuilder, tokenList)

    override fun createCustomParser(
        nodeBuilder: NodeBuilder,
        tokenList: List<Token>,
        expressionParser: ExpressionParsingBuilder,
        statementParser: StatementParser,
        current: Int,
    ): ParserInterface = Parser(tokenList, nodeBuilder, expressionParser, statementParser, current)
}
