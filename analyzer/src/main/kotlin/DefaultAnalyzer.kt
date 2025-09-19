import diagnostic.Diagnostic
import parser.result.FinalResult
import rules.ProgramSummary
import rules.Rule

class DefaultAnalyzer(
    private val rules: List<Rule>,
) : Analyzer {

    override fun analyze(result: FinalResult): List<Diagnostic> {
        val program = result.getProgram()
        val summary = ProgramSummary.from(program)

        val diags = mutableListOf<Diagnostic>()
        for (rule in rules) {
            if (!rule.canHandle(summary)) continue
            if (!rule.canHandle(program)) continue
            diags += rule.apply(program)
        }
        return diags
    }
}
