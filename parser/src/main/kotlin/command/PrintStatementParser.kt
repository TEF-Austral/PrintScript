package parser.command

import TokenType
import node.Statement
import parser.RecursiveDescentParser

class PrintStatementParser : StatementParserCommand {

    override fun canHandle(type: TokenType): Boolean {
        return type == TokenType.PRINT
    }

    override fun parse(parser: RecursiveDescentParser): Statement {
        parser.consume("PRINT", "Expected 'print'")
        parser.consume("LEFT_PAREN", "Expected '(' after 'print'")
        val expression = parser.getExpressionParser().parseExpression(parser)
        parser.consume("RIGHT_PAREN", "Expected ')' after expression")
        parser.consume("SEMICOLON", "Expected ';' after print statement")

        return parser.getNodeBuilder().buildPrintStatementNode(expression)
    }
}