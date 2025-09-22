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
                    "Parser Error: " + result.message(),
                    result.getProgram().getCoordinates(),
                ),
            )
        }
        return rules.apply(result.getProgram())
    }
}
