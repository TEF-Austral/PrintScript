package parser.command

import TokenType
import node.Statement
import parser.RecursiveDescentParser

class PrintStatementParser : StatementParserCommand {

    override fun canHandle(type: TokenType): Boolean {
        return type == TokenType.PRINT
    }

    override fun parse(parser: RecursiveDescentParser): Statement {
        parser.consume(TokenType.PRINT)
        parser.consume(TokenType.DELIMITERS) // (
        val expression = parser.getExpressionParser().parseExpression(parser)
        parser.consume(TokenType.DELIMITERS) // )
        parser.consume(TokenType.DELIMITERS) // ;
        return parser.getNodeBuilder().buildPrintStatementNode(expression)
    }

    // TODO: Soporte de múltiples Delimiters, porque aca debria de haber o quisa no un
    // chequeo de cada uno digamos en orden ( , ), ;
}