package statement.enforcers

import parser.Parser
import parser.result.SemanticError
import parser.result.SemanticResult
import parser.result.SemanticSuccess

class ConstAssignmentEnforcer(
    private val nextEnforcer: SemanticEnforcers,
) : SemanticEnforcers {
    override fun enforce(
        previousParser: Parser,
        result: SemanticResult,
    ): SemanticResult {
        val currentParser = result.getParser()
        if (!result.isSuccess()) {
            val token = currentParser.peak()
            val coordinates = previousParser.peak()!!.getCoordinates()
            return SemanticError(
                "Expected Const Assignment Structure but found ${token?.getValue()}",
                result.identifier(),
                result.dataType(),
                result.initialValue(),
                currentParser,
                coordinates,
            )
        }
        if (!currentParser.hasNext()) {
            val coordinates = previousParser.peak()!!.getCoordinates()
            return SemanticError(
                "Expected expression after const assignment, but no more tokens found",
                result.identifier(),
                result.dataType(),
                result.initialValue(),
                currentParser,
                coordinates,
            )
        }
        val parserResult =
            currentParser.getExpressionParser().parseExpression(
                currentParser.advance(),
            )
        return nextEnforcer.enforce(
            currentParser,
            SemanticSuccess(
                parserResult.message(),
                result.identifier(),
                result.dataType(),
                parserResult.getExpression(),
                parserResult.getParser(),
            ),
        )
    }
}
