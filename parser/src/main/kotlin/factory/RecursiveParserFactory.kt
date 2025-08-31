package parser.factory

import Token
import builder.NodeBuilder
import parser.statement.StatementParser
import parser.Parser
import parser.expression.ExpressionParser
import parser.expression.binary.ParseBinaryFactory
import parser.expression.primary.TokenConverterFactory

class RecursiveParserFactory : ParserFactory {
    override fun createParser(
        tokens: List<Token>,
        nodeBuilder: NodeBuilder,
    ): Parser {
        val tokenToExpression = TokenConverterFactory.createDefaultRegistry()
        val parseBinary = ParseBinaryFactory.create()
        val expressionParser = ExpressionParser(tokenToExpression, parseBinary)
        val statementParser = StatementParser()
        return Parser(tokens, nodeBuilder, expressionParser, statementParser, 0)
    }
}
