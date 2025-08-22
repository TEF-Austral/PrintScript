package parser.command

import Token
import TokenType
import node.expression.Expression
import node.statement.Statement
import parser.Parser

class VariableDeclarationParser : StatementParserCommand {
    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean = token?.getType() == TokenType.DECLARATION

    override fun parse(parser: Parser): Statement {
        parser.consume(TokenType.DECLARATION) // "let" or "const"
        val identifier = parser.consume(TokenType.IDENTIFIER)
        parser.consume(TokenType.DELIMITERS) // :
        val dataType = parser.consume(TokenType.DATA_TYPES)

        var initialValue: Expression? = null
        if (parser.match(TokenType.ASSIGNMENT)) {
            initialValue = parser.getExpressionParser().parseExpression(parser)
        }

        parser.consume(TokenType.DELIMITERS) // ;
        return parser.getNodeBuilder().buildVariableDeclarationStatementNode(identifier, dataType, initialValue)
    }
}
