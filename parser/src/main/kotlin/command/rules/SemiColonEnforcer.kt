package command.rules

import parser.Parser
import parser.result.SemanticError
import parser.result.SemanticResult
import parser.result.SemanticSuccess
import type.CommonTypes

class SemiColonEnforcer : SemanticEnforcers {
    override fun enforce(result: SemanticResult): SemanticResult {
        val currentParser = result.getParser()
        if (isSemiColon(currentParser) || !result.isSuccess()) {
            return SemanticError(
                "Variable declaration must end in ; " + result.message(),
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

    private fun isSemiColon(currentParser: Parser): Boolean = !currentParser.consume(CommonTypes.DELIMITERS).isSuccess() || currentParser.peak()?.getValue() != ";"
}
