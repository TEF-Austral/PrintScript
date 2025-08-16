package parser.statement

import node.Statement
import parser.RecursiveDescentParser
import parser.command.AssignmentParser
import parser.command.BlockStatementParser
import parser.command.ExpressionStatementParser
import parser.command.PrintStatementParser
import parser.command.VariableDeclarationParser

class DefaultStatementParser(StatementParserRegistry) : StatementParser {

    private val declarationParser = VariableDeclarationParser()
    private val assignmentParser = AssignmentParser()
    private val printParser = PrintStatementParser()
    private val blockParser = BlockStatementParser()
    private val expressionStatementParser = ExpressionStatementParser()

    override fun parseStatement(parser: RecursiveDescentParser): Statement {

        return when {
            parser.check("LET") || parser.check("CONST") -> declarationParser.parse(parser)
            parser.check("PRINT") -> printParser.parse(parser)
            parser.check("LEFT_BRACE") -> blockParser.parse(parser)
            isAssignment(parser) -> assignmentParser.parse(parser)
            else -> expressionStatementParser.parse(parser)
        }

    }

    private fun isAssignment(parser: RecursiveDescentParser): Boolean {

        if (!parser.check("IDENTIFIER")) return false

        val savedPosition = parser.current
        parser.advance()
        val isAssign = parser.check("ASSIGN")
        parser.current = savedPosition // restore position

        return isAssign

    }
}