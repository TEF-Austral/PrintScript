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
            if (token == null) {
                val coordinates = previousParser.peak()!!.getCoordinates()
                return SemanticError(
                    "Expected Const Assignment Structure at end of file " + coordinates.getRow() +
                        ":" +
                        coordinates.getColumn() +
                        " " +
                        result.message(),
                    result.identifier(),
                    result.dataType(),
                    result.initialValue(),
                    currentParser,
                )
            }
            val coordinates = token.getCoordinates()
            return SemanticError(
                "Expected Const Assignment Structure at " + coordinates.getRow() + ":" +
                    coordinates.getColumn() +
                    " " +
                    result.message(),
                result.identifier(),
                result.dataType(),
                result.initialValue(),
                currentParser,
            )
        }
        if (!currentParser.hasNext()) {
            val token = currentParser.peak()
            if (token == null) {
                val coordinates = previousParser.peak()!!.getCoordinates()
                return SemanticError(
                    "Expected expression after const assignment at end of file " +
                        coordinates.getRow() +
                        ":" +
                        coordinates.getColumn() +
                        ", but no more tokens found.",
                    result.identifier(),
                    result.dataType(),
                    result.initialValue(),
                    currentParser,
                )
            }
            val coordinates = token.getCoordinates()
            return SemanticError(
                "Expected expression after const assignment at " + coordinates.getRow() + ":" +
                    coordinates.getColumn() +
                    ", but no more tokens found.",
                result.identifier(),
                result.dataType(),
                result.initialValue(),
                currentParser,
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
