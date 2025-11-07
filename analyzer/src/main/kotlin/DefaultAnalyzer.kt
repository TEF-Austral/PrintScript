import coordinates.Position
import diagnostic.Diagnostic
import parser.result.FinalResult
import rules.RuleEnforcer

class DefaultAnalyzer(
    private val rules: RuleEnforcer,
) : Analyzer {
    override fun analyze(result: FinalResult): List<Diagnostic> {
        if (!result.isSuccess()) {
            return listOf(
                Diagnostic(
                    "Failed to Parse: " + result.message(),
                    result.getCoordinates() ?: Position(-1, -1),
                ),
            )
        }
        return rules.apply(result.getProgram())
    }
}
