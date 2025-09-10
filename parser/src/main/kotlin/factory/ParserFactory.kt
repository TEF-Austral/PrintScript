package parser.factory

import Token
import builder.DefaultNodeBuilder
import builder.NodeBuilder
import parser.ParserInterface
import parser.statement.StatementParser
import parser.statement.expression.ExpressionParsingBuilder
import type.Version

sealed interface ParserFactory {

    fun createWithVersion(version: Version, nodeBuilder: NodeBuilder = DefaultNodeBuilder(),tokenList: List<Token> = emptyList()): ParserInterface

    fun createDefault(nodeBuilder: NodeBuilder = DefaultNodeBuilder(),tokenList: List<Token> = emptyList()): ParserInterface

    fun createCustomParser(nodeBuilder: NodeBuilder, tokenList: List<Token>,expressionParser: ExpressionParsingBuilder,statementParser: StatementParser,current: Int): ParserInterface

}
