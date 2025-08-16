package parser.command

import TokenType
import node.Statement
import parser.RecursiveDescentParser

class AssignmentParser : StatementParserCommand {

    override fun canHandle(type: TokenType): Boolean {
        return type == TokenType.ASSIGNMENT
    }

    override fun parse(parser: RecursiveDescentParser): Statement {
        val identifier = parser.consume("IDENTIFIER", "Expected variable name")
        parser.consume("ASSIGN", "Expected '=' in assignment")
        val value = parser.getExpressionParser().parseExpression(parser)
        parser.consume("SEMICOLON", "Expected ';' after assignment")

        return parser.getNodeBuilder().buildAssignmentStatementNode(identifier, value)
    }
}