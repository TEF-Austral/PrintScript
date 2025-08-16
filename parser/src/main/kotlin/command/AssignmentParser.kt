package parser.command

import TokenType
import node.Statement
import parser.RecursiveDescentParser

class AssignmentParser : StatementParserCommand {

    override fun canHandle(type: TokenType): Boolean {
        return type == TokenType.ASSIGNMENT
    }

    //    return when {
//        parser.check("LET") || parser.check("CONST") -> declarationParser.parse(parser)
//        parser.check("PRINT") -> printParser.parse(parser)
//        parser.check("LEFT_BRACE") -> blockParser.parse(parser)
//        isAssignment(parser) -> assignmentParser.parse(parser)
//        else -> expressionStatementParser.parse(parser)
//    }

    //    private fun isAssignment(parser: RecursiveDescentParser): Boolean {
//        // Look ahead para determinar si es una asignación
//        if (!parser.check("IDENTIFIER")) return false

//        val savedPosition = parser.current
//        parser.advance()
//        val isAssign = parser.check("ASSIGN")
//        parser.current = savedPosition // restore position
//
//        return isAssign
//    }

    override fun parse(parser: RecursiveDescentParser): Statement {
        val identifier = parser.consume("IDENTIFIER", "Expected variable name")
        parser.consume("ASSIGN", "Expected '=' in assignment")
        val value = parser.getExpressionParser().parseExpression(parser)
        parser.consume("SEMICOLON", "Expected ';' after assignment")

        return parser.getNodeBuilder().buildAssignmentStatementNode(identifier, value)
    }
}