package parser.command

import Token
import TokenType
import node.Expression
import node.Statement
import parser.Parser
import parser.RecursiveDescentParser

class VariableDeclarationParser : StatementParserCommand {

    override fun canHandle(token: Token?, parser: Parser): Boolean {
        return token?.getType() == TokenType.DECLARATION
    }

    override fun parse(parser: RecursiveDescentParser): Statement {
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