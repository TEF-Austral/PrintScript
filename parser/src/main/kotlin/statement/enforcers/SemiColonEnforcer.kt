package statement.enforcers

import parser.Parser
import parser.result.SemanticError
import parser.result.SemanticResult
import parser.result.SemanticSuccess
import parser.utils.isSemiColon
import type.CommonTypes

class SemiColonEnforcer : SemanticEnforcers {
    override fun enforce(
        previousParser: Parser,
        result: SemanticResult,
    ): SemanticResult {
        val currentParser = result.getParser()
        if (!currentParser.hasNext() || !isSemiColon(currentParser.peak()) || !result.isSuccess()) {
            val token = currentParser.peak()
            if (token == null) {
                val coordinates = previousParser.peak()!!.getCoordinates()
                return SemanticError(
                    "Variable declaration must end in ; at end of file " + coordinates.getRow() +
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
                "Variable declaration must end in ; at " + coordinates.getRow() + ":" +
                    coordinates.getColumn() +
                    " " +
                    result.message(),
                result.identifier(),
                result.dataType(),
                result.initialValue(),
                currentParser,
            )
        }
        val parserResult = currentParser.consume(CommonTypes.DELIMITERS)
        return SemanticSuccess(
            parserResult.message(),
            result.identifier(),
            result.dataType(),
            result.initialValue(),
            parserResult.getParser(),
        )
    }
}
