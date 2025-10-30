package parser.statement.expression

import Token
import parser.Parser
import parser.result.ExpressionBuiltResult
import parser.result.ExpressionResult
import parser.utils.isSemiColon
import parser.utils.isValidResultAndCurrentToken
import type.CommonTypes

class ReadInputParser : ExpressionBuilder {
    override fun parse(
        parser: Parser,
        current: Token,
    ): ExpressionResult {
        val token = parser.advance().advance().peak()
        if (isSemiColon(token)) {
            val coordinates = token?.getCoordinates()
            throw Exception(
                "Invalid structure " +
                    coordinates?.getRow() + ":" + coordinates?.getColumn(),
            )
        }
        val readInput = parser.consume(CommonTypes.READ_INPUT)
        if (!isValidResultAndCurrentToken(readInput)) {
            val currentToken = readInput.getParser().peak()
            val coordinates = currentToken?.getCoordinates()
            throw Exception(
                "Expected readInput " +
                    coordinates?.getRow() + ":" + coordinates?.getColumn(),
            )
        }
        val value =
            readInput.getParser().getExpressionParser().parseExpression(
                readInput.getParser(),
            )
        val literalExpression = value.getExpression()
        val builtStatement =
            value.getParser().getNodeBuilder().buildReadInputNode(
                literalExpression,
            )
        return ExpressionBuiltResult(value.getParser(), builtStatement)
    }

    override fun canHandle(types: CommonTypes): Boolean = types == CommonTypes.READ_INPUT
}
