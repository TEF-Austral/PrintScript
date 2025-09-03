package parser.command

import Token
import parser.Parser
import parser.result.StatementBuiltResult
import parser.result.StatementResult
import parser.utils.checkType
import parser.utils.isSemiColon
import parser.utils.isValidResultAndCurrentToken
import type.CommonTypes

class AssignmentParser : StatementCommand {
    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean {
        if (token?.getType() != CommonTypes.IDENTIFIER) return false
        val newParser = parser.advance()
        val isAssign = checkType(CommonTypes.ASSIGNMENT, newParser.peak())
        return isAssign
    }

    override fun parse(parser: Parser): StatementResult {
        val identifier = parser.consume(CommonTypes.IDENTIFIER)
        if (!isValidResultAndCurrentToken(identifier)) {
            throw Exception("Expected identifier")
        }
        val assigmentParser = identifier.getParser().consume(CommonTypes.ASSIGNMENT).getParser() // =
        val value = assigmentParser.getExpressionParser().parseExpression(assigmentParser.advance())
        val delimiterParser =
            if (isSemiColon(value.getParser().peak())) {
                value.getParser().consume(CommonTypes.DELIMITERS).getParser()
            } else {
                value.getParser()
            }
        val builtStatement = delimiterParser.getNodeBuilder().buildAssignmentStatementNode(identifier.getParser().peak()!!, value.getExpression())
        return StatementBuiltResult(delimiterParser, builtStatement)
    }
}
