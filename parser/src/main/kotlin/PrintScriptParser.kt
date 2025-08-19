package parser

import Token
import TokenType
import builder.NodeBuilder
import node.Program
import node.statement.Statement
import parser.expression.ExpressionParser
import parser.statement.StatementParser

class PrintScriptParser(private val tokens: List<Token>, private val nodeBuilder: NodeBuilder,
                        private val expressionParser: ExpressionParser, private val statementParser: StatementParser) : Parser {

    override var current = 0

    override fun parse(): Program {

        val statements = mutableListOf<Statement>()

        while (!isAtEnd()) {
            val statement = statementParser.parseStatement(this)
            statements.add(statement)
        }

        return nodeBuilder.buildProgramNode(statements)
    }

    override fun getCurrentToken(): Token? = if (isAtEnd()) null else tokens[current]

    override fun advance(): Token? {
        if (!isAtEnd()) current++
        return previous()
    }

    override fun previous(): Token? = if (current == 0) null else tokens[current - 1]

    override fun isAtEnd(): Boolean = current >= tokens.size

    override fun check(type: TokenType): Boolean = if (isAtEnd()) false else getCurrentToken()?.getType() == type

    override fun match(vararg types: TokenType): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    override fun consume(type: TokenType): Token {
        if (check(type)) return advance()!!
        throw IllegalArgumentException("Expected token type $type but found ${getCurrentToken()?.getType()}")
    }

    override fun getExpressionParser(): ExpressionParser = expressionParser
    override fun getNodeBuilder(): NodeBuilder = nodeBuilder
}