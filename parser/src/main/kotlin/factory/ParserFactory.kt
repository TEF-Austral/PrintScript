package parser.factory

import Token
import builder.NodeBuilder
import parser.Parser

interface ParserFactory {
    fun createParser(
        tokens: List<Token>,
        nodeBuilder: NodeBuilder,
    ): Parser
}
