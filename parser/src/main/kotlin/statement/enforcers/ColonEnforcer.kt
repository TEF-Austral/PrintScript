package statement.enforcers

import parser.Parser
import parser.result.SemanticError
import parser.result.SemanticResult
import parser.result.SemanticSuccess
import type.CommonTypes

class ColonEnforcer(
    private val nextEnforcer: SemanticEnforcers,
) : SemanticEnforcers {
    override fun enforce(
        previousParser: Parser,
        result: SemanticResult,
    ): SemanticResult {
        val currentParser = result.getParser()
        if (!currentParser.hasNext() || isColon(currentParser) || !result.isSuccess()) {
            val token = currentParser.peak()
            if (token == null) {
                val coordinates = previousParser.peak()!!.getCoordinates()
                return SemanticError(
                    "Expected delimiter at end of file " + coordinates.getRow() + ":" +
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
                "Expected delimiter at " + coordinates.getRow() + ":" + coordinates.getColumn() +
                    " " +
                    result.message(),
                result.identifier(),
                result.dataType(),
                result.initialValue(),
                currentParser,
            )
        }
        val parserResult = currentParser.consume(CommonTypes.DELIMITERS)
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

    private fun isColon(currentParser: Parser): Boolean =
        !currentParser.consume(CommonTypes.DELIMITERS).isSuccess() ||
            (
                currentParser.hasNext() &&
                    currentParser
                        .peak()
                        ?.getValue()
                        ?.trimStart()
                        ?.trimEnd() != ":"
            )
}
