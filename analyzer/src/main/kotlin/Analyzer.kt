import diagnostic.Diagnostic
import parser.result.FinalResult

interface Analyzer {
    fun analyze(result: FinalResult): List<Diagnostic>
}
