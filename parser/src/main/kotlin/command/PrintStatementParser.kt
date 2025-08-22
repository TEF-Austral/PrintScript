package parser.command

import Token
import TokenType
import node.statement.Statement
import parser.Parser

class PrintStatementParser : StatementParserCommand {
    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean = token?.getType() == TokenType.PRINT

    override fun parse(parser: Parser): Statement {
        parser.consume(TokenType.PRINT)
        parser.consume(TokenType.DELIMITERS) // (
        val expression = parser.getExpressionParser().parseExpression(parser)
        parser.consume(TokenType.DELIMITERS) // )
        parser.consume(TokenType.DELIMITERS) // ;
        return parser.getNodeBuilder().buildPrintStatementNode(expression)
    }
    // TODO: Soporte de múltiples Delimiters, porque aca debria de haber o quisa no un
}
