package parser.factory

import Token
import builder.NodeBuilder
import parser.expression.DefaultExpressionParser
import parser.statement.DefaultStatementParser
import parser.Parser
import parser.RecursiveDescentParser

class RecursiveDescentParserFactory : ParserFactory {
    override fun createParser(tokens: List<Token>, nodeBuilder: NodeBuilder): Parser {
        val expressionParser = DefaultExpressionParser()
        val statementParser = DefaultStatementParser()
        return RecursiveDescentParser(tokens, nodeBuilder, expressionParser, statementParser)
    }
}