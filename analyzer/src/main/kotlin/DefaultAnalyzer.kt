import diagnostic.Diagnostic
import parser.result.FinalResult
import rules.Rule

class DefaultAnalyzer(
    private val rules: List<Rule>,
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
        return rules.flatMap { it.apply(result.getProgram()) }
    }
}
