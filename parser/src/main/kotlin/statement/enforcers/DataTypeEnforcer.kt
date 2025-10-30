package statement.enforcers

import parser.Parser
import parser.result.SemanticError
import parser.result.SemanticResult
import parser.result.SemanticSuccess
import parser.utils.verifyCurrentToken
import type.CommonTypes

class DataTypeEnforcer(
    private val nextEnforcer: SemanticEnforcers,
    private val possibleTypes: List<CommonTypes> = listOf(CommonTypes.NUMBER, CommonTypes.STRING),
) : SemanticEnforcers {
    override fun enforce(
        previousParser: Parser,
        result: SemanticResult,
    ): SemanticResult {
        val currentParser = result.getParser()
        if (!currentParser.hasNext() ||
            !(verifyCurrentToken(possibleTypes, currentParser)) ||
            !result.isSuccess()
        ){
            val token = currentParser.peak()
            if (token == null) {
                val coordinates = previousParser.peak()!!.getCoordinates()
                return SemanticError(
                    "Expected data type at end of file " + coordinates.getRow() + ":" +
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
                "Expected data type at " + coordinates.getRow() + ":" + coordinates.getColumn() +
                    " " +
                    result.message(),
                result.identifier(),
                result.dataType(),
                result.initialValue(),
                currentParser,
            )
        }
        return nextEnforcer.enforce(
            currentParser,
            SemanticSuccess(
                result.message(),
                result.identifier(),
                currentParser.peak()!!,
                result.initialValue(),
                currentParser.advance(),
            ),
        )
    }
}
