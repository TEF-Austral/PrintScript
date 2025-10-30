package parser.statement.expression

import Token
import node.LiteralExpression
import parser.Parser
import parser.result.ExpressionBuiltResult
import parser.result.ExpressionResult
import parser.utils.isSemiColon
import parser.utils.isValidResultAndCurrentToken
import type.CommonTypes

class ReadEnvParser : ExpressionBuilder {
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
        val readEnv = parser.consume(CommonTypes.READ_ENV)
        if (!isValidResultAndCurrentToken(readEnv)) {
            val currentToken = readEnv.getParser().peak()
            val coordinates = currentToken?.getCoordinates()
            throw Exception(
                "Expected readEnv " +
                    coordinates?.getRow() + ":" + coordinates?.getColumn(),
            )
        }
        val value = readEnv.getParser().getExpressionParser().parseExpression(readEnv.getParser())
        if (value.getExpression() !is LiteralExpression) {
            val currentToken = value.getParser().peak()
            val coordinates = currentToken?.getCoordinates()
            throw Exception(
                "Only a literal expression can be inside of readEnv " +
                    coordinates?.getRow() + ":" + coordinates?.getColumn(),
            )
        }
        val literalExpression = value.getExpression() as LiteralExpression
        val builtStatement = value.getParser().getNodeBuilder().buildReadEnvNode(literalExpression)
        return ExpressionBuiltResult(value.getParser(), builtStatement)
    }

    override fun canHandle(types: CommonTypes): Boolean = types == CommonTypes.READ_ENV
}
