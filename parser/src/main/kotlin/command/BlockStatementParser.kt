package parser.command

import TokenType
import node.Statement
import parser.statement.DefaultStatementParser
import parser.RecursiveDescentParser

class BlockStatementParser : StatementParserCommand {

    override fun canHandle(type: TokenType): Boolean {
        return type == TokenType.DELIMITER
    }

    override fun parse(parser: RecursiveDescentParser): Statement {
        parser.consume("LEFT_BRACE", "Expected '{'")
        val statements = mutableListOf<Statement>()

        while (!parser.check("RIGHT_BRACE") && !parser.isAtEnd()) {
            val statementParser = DefaultStatementParser()
            statements.add(statementParser.parseStatement(parser))
        }

        parser.consume("RIGHT_BRACE", "Expected '}'")
        return parser.getNodeBuilder().buildBlockStatementNode(statements)
    }
}