package parser.command

import TokenType
import node.Statement
import parser.RecursiveDescentParser

class ExpressionStatementParser : StatementParserCommand {

    override fun canHandle(type: TokenType): Boolean {
        return type == TokenType.DELIMITERS
    }

    override fun parse(parser: RecursiveDescentParser): Statement {
        val expression = parser.getExpressionParser().parseExpression(parser)
        parser.consume("SEMICOLON", "Expected ';' after expression")
        return parser.getNodeBuilder().buildExpressionStatementNode(expression)
    }
}