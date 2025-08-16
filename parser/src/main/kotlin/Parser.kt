package parser

import Token
import builder.NodeBuilder
import node.*
import parser.expression.ExpressionParser
import parser.statement.StatementParser

interface Parser {
    fun parse(): Program
}

class RecursiveDescentParser(
    private val tokens: List<Token>,
    private val nodeBuilder: NodeBuilder,
    private val expressionParser: ExpressionParser,
    private val statementParser: StatementParser
) : Parser {

    private var current = 0

    override fun parse(): Program {

        val statements = mutableListOf<Statement>()

        while (!isAtEnd()) {
            val statement = statementParser.parseStatement(this)
            statements.add(statement)
        }

        return nodeBuilder.buildProgramNode(statements)
    }

    fun getCurrentToken(): Token? = if (isAtEnd()) null else tokens[current]

    fun advance(): Token? {
        if (!isAtEnd()) current++
        return previous()
    }

    fun previous(): Token? = if (current == 0) null else tokens[current - 1]

    fun isAtEnd(): Boolean = current >= tokens.size

    fun check(type: String): Boolean = if (isAtEnd()) false else getCurrentToken()?.getType() == type

    fun match(vararg types: String): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    fun consume(type: String, message: String): Token {
        if (check(type)) return advance()!!
    }

    fun getExpressionParser(): ExpressionParser = expressionParser
    fun getNodeBuilder(): NodeBuilder = nodeBuilder

}
