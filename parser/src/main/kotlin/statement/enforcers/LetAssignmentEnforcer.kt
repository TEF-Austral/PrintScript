package statement.enforcers

import parser.Parser
import parser.result.SemanticError
import parser.result.SemanticResult
import parser.result.SemanticSuccess
import type.CommonTypes

class LetAssignmentEnforcer(
    private val nextEnforcer: SemanticEnforcers,
) : SemanticEnforcers {
    override fun enforce(
        previousParser: Parser,
        result: SemanticResult,
    ): SemanticResult {
        val currentParser = result.getParser()
        if (!result.isSuccess() || !currentParser.consume(CommonTypes.ASSIGNMENT).isSuccess()) {
            val token = currentParser.peak()
            val coordinates = previousParser.peak()!!.getCoordinates()
            return SemanticError(
                "Expected = but found ${token?.getValue()}",
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
