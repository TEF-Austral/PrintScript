package statement.enforcers

import parser.Parser
import parser.result.SemanticResult

sealed interface SemanticEnforcers {
    fun enforce(
        previousParser: Parser,
        result: SemanticResult,
    ): SemanticResult
}
