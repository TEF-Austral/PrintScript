package parser

import Token
import TokenType
import builder.NodeBuilder
import node.*
import parser.expression.ExpressionParser

interface Parser {

    var current: Int

    fun parse(): Program

    fun consume(type: TokenType): Token

    fun advance(): Token?

    fun check(type: TokenType): Boolean

    fun getCurrentToken(): Token?

    fun previous(): Token?

    fun isAtEnd(): Boolean

    fun match(vararg types: TokenType): Boolean

    fun getExpressionParser(): ExpressionParser

    fun getNodeBuilder(): NodeBuilder
}
