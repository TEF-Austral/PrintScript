package parser.factory

import Token
import builder.NodeBuilder
import parser.Parser
import parser.ParserInterface

sealed interface ParserFactory {
    fun createParser(
        tokens: List<Token>,
        nodeBuilder: NodeBuilder,
    ): Parser

    fun withNewTokens(
        tokens: List<Token>,
        parser: ParserInterface,
    ): Parser
}
