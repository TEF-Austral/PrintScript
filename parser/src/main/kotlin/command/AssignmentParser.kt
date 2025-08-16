package parser.command

import Token
import TokenType
import node.Statement
import parser.Parser
import parser.RecursiveDescentParser

class AssignmentParser : StatementParserCommand {

    override fun canHandle(token: Token?, parser: Parser): Boolean {
        if (token?.getType() != TokenType.IDENTIFIER) return false
        val savedPosition = parser.current
        parser.advance()
        val isAssign = parser.check(TokenType.ASSIGNMENT)
        parser.current = savedPosition
        return isAssign
    }


    override fun parse(parser: RecursiveDescentParser): Statement {
        val identifier = parser.consume(TokenType.IDENTIFIER)
        parser.consume(TokenType.ASSIGNMENT) // =
        val value = parser.getExpressionParser().parseExpression(parser)
        parser.consume(TokenType.DELIMITERS) // ;
        return parser.getNodeBuilder().buildAssignmentStatementNode(identifier, value)
    }
}