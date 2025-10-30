package statement.enforcers

import parser.Parser
import parser.result.SemanticError
import parser.result.SemanticResult
import parser.result.SemanticSuccess
import type.CommonTypes

class ConstEnforcer(
    private val nextEnforcer: SemanticEnforcers,
) : SemanticEnforcers {
    override fun enforce(
        previousParser: Parser,
        result: SemanticResult,
    ): SemanticResult {
        val currentParser = result.getParser()
        if (checkForConstAndValidState(result)) {
            val token = currentParser.peak()
            if (token == null) {
                val coordinates = previousParser.peak()!!.getCoordinates()
                return SemanticError(
                    "Expected const declaration at end of file " + coordinates.getRow() + ":" +
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
                "Expected const declaration at " + coordinates.getRow() + ":" +
                    coordinates.getColumn() +
                    " " +
                    result.message(),
                result.identifier(),
                result.dataType(),
                result.initialValue(),
                currentParser,
            )
        }
        val parserResult = currentParser.consume(CommonTypes.CONST)
        return nextEnforcer.enforce(
            currentParser,
            SemanticSuccess(
                parserResult.message(),
                result.identifier(),
                result.dataType(),
                result.initialValue(),
                parserResult.getParser(),
            ),
        )
    }

    private fun checkForConstAndValidState(result: SemanticResult): Boolean {
        val currentParser = result.getParser()
        return !currentParser.consume(CommonTypes.CONST).isSuccess() || !result.isSuccess()
    }
}
