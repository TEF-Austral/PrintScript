package statement.enforcers

import parser.Parser
import parser.result.SemanticError
import parser.result.SemanticResult
import parser.result.SemanticSuccess
import type.CommonTypes

class LetEnforcer(
    private val nextEnforcer: SemanticEnforcers,
) : SemanticEnforcers {
    override fun enforce(
        previousParser: Parser,
        result: SemanticResult,
    ): SemanticResult {
        val currentParser = result.getParser()
        if (checkForLetAndValidState(result)) {
            val token = currentParser.peak()
            if (token == null) {
                val coordinates = previousParser.peak()!!.getCoordinates()
                return SemanticError(
                    "Expected let declaration at end of file " + coordinates.getRow() + ":" +
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
                "Expected let declaration at " + coordinates.getRow() + ":" +
                    coordinates.getColumn() +
                    " " +
                    result.message(),
                result.identifier(),
                result.dataType(),
                result.initialValue(),
                currentParser,
            )
        }
        val parserResult = currentParser.consume(CommonTypes.LET)
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

    private fun checkForLetAndValidState(result: SemanticResult): Boolean {
        val currentParser = result.getParser()
        return !currentParser.consume(CommonTypes.LET).isSuccess() || !result.isSuccess()
    }
}
