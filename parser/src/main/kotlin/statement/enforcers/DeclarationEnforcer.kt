package statement.enforcers

import parser.result.SemanticError
import parser.result.SemanticResult
import parser.result.SemanticSuccess
import type.CommonTypes

class DeclarationEnforcer(
    private val nextEnforcer: SemanticEnforcers,
) : SemanticEnforcers {
    override fun enforce(result: SemanticResult): SemanticResult {
        val currentParser = result.getParser()
        if (!currentParser.consume(CommonTypes.LET).isSuccess() || !result.isSuccess()) { // TODO CAMBIAR ESTO
            return SemanticError(
                "Expected declaration " + result.message(),
                result.identifier(),
                result.dataType(),
                result.initialValue(),
                currentParser,
            )
        }
        val parserResult = currentParser.consume(CommonTypes.LET) // TODO CAMBIAR ESTO
        return nextEnforcer.enforce(
            SemanticSuccess(
                parserResult.message(),
                result.identifier(),
                result.dataType(),
                result.initialValue(),
                parserResult.getParser(),
            ),
        )
    }
}
