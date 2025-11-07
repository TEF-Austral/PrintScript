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
            val coordinates = previousParser.peak()!!.getCoordinates()
            return SemanticError(
                "Expected const declaration but found ${token?.getValue()}",
                result.identifier(),
                result.dataType(),
                result.initialValue(),
                currentParser,
                coordinates,
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
