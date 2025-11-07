package parser.statement

import Token
import parser.Parser
import parser.exception.ParserException
import parser.result.StatementBuiltResult
import parser.result.StatementResult
import parser.utils.advancePastSemiColon
import parser.utils.checkType
import parser.utils.isSemiColon
import parser.utils.isValidResultAndCurrentToken
import type.CommonTypes

class AssignmentParser : StatementBuilder {
    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean {
        if (!checkType(CommonTypes.IDENTIFIER, token)) return false
        return checkType(CommonTypes.ASSIGNMENT, parser.advance().peak())
    }

    override fun parse(parser: Parser): StatementResult {
        val possibleSemiColon = parser.advance().advance().peak()
        if (isSemiColon(possibleSemiColon)) {
            val coordinates = possibleSemiColon!!.getCoordinates()
            throw ParserException("Invalid structure", coordinates)
        }
        val identifier = parser.consume(CommonTypes.IDENTIFIER)
        if (!isValidResultAndCurrentToken(identifier)) {
            val coordinates = parser.peak()!!.getCoordinates()
            throw ParserException("Expected identifier", coordinates)
        }
        val assigmentParser =
            identifier
                .getParser()
                .consume(
                    CommonTypes.ASSIGNMENT,
                ).getParser()
        val value = assigmentParser.getExpressionParser().parseExpression(assigmentParser)
        val delimiterParser = advancePastSemiColon(value.getParser())
        val builtStatement =
            delimiterParser.getNodeBuilder().buildAssignmentStatementNode(
                parser.peak()!!,
                value.getExpression(),
            )
        return StatementBuiltResult(delimiterParser, builtStatement)
    }
}
