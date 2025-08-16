package parser.statement

import node.Statement
import parser.RecursiveDescentParser

class DefaultStatementParser(private val statementParser: StatementParser) : StatementParser {

    override fun parseStatement(parser: RecursiveDescentParser): Statement {
        return statementParser.parseStatement(parser)
    }

//    return when {
//        parser.check("LET") || parser.check("CONST") -> declarationParser.parse(parser)
//        parser.check("PRINT") -> printParser.parse(parser)
//        parser.check("LEFT_BRACE") -> blockParser.parse(parser)
//        isAssignment(parser) -> assignmentParser.parse(parser)
//        else -> expressionStatementParser.parse(parser)
//    }


}