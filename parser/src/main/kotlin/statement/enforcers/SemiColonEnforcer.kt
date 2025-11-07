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
        val token = currentParser.peak()
        if (!currentParser.hasNext() || !isSemiColon(currentParser.peak()) || !result.isSuccess()) {
            val message = "Variable declaration must end in ; but found ${token?.getValue()}"
            val coordinates = previousParser.peak()!!.getCoordinates()
            return SemanticError(
                message,
                result.identifier(),
                result.dataType(),
                result.initialValue(),
                currentParser,
                coordinates,
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
