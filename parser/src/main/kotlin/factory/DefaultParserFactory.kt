package parser.factory

import Token
import builder.NodeBuilder
import parser.Parser
import parser.ParserInterface

class DefaultParserFactory : ParserFactory{

    override fun createParser(tokens: List<Token>, nodeBuilder: NodeBuilder): Parser {
        TODO("Not yet implemented")
    }

    override fun withNewTokens(tokens: List<Token>, parser: ParserInterface): Parser {
        TODO("Not yet implemented")
    }
}