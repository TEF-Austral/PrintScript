import diagnostic.Diagnostic
import node.Program
import rules.Rule

class DefaultAnalyzer(
    private val rules: List<Rule>,
) : Analyzer {
    override fun analyze(program: Program): List<Diagnostic> = rules.flatMap { it.apply(program) }
}
