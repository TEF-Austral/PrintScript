package parser.factory

import Token
import builder.NodeBuilder
import parser.statement.DefaultStatementParser
import parser.Parser
import parser.PrintScriptParser
import parser.expression.DefaultExpressionParser
import parser.expression.binary.PrintScriptParseBinary
import parser.expression.primary.TokenToExpressionFactory

class RecursiveParserFactory : ParserFactory {

    override fun createParser(tokens: List<Token>, nodeBuilder: NodeBuilder): Parser {
        val tokenToExpression = TokenToExpressionFactory.createDefaultRegistry()
        val parseBinary = PrintScriptParseBinary.create()
        val expressionParser = DefaultExpressionParser(tokenToExpression, parseBinary)
        val statementParser = DefaultStatementParser()
        return PrintScriptParser(tokens, nodeBuilder, expressionParser, statementParser)
    }
}