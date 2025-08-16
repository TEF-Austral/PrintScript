package parser.command

import TokenType
import node.Expression
import node.Statement
import parser.RecursiveDescentParser

class VariableDeclarationParser : StatementParserCommand {

    override fun canHandle(type: TokenType): Boolean {
        return type == TokenType.DELIMITERS
    }

    override fun parse(parser: RecursiveDescentParser): Statement {
        val declarationType = parser.advance()!! // LET or CONST
        val identifier = parser.consume("IDENTIFIER", "Expected variable name")
        parser.consume("COLON", "Expected ':' after variable name")
        val dataType = parser.consume("TYPE", "Expected data type")

        var initialValue: Expression? = null
        if (parser.match("ASSIGN")) {
            initialValue = parser.getExpressionParser().parseExpression(parser)
        }

        parser.consume("SEMICOLON", "Expected ';' after variable declaration")
        return parser.getNodeBuilder().buildVariableDeclarationStatementNode(identifier, dataType, initialValue)
    }
}