import analyzer.Diagnostic
import node.Program

interface Analyzer {
    fun analyze(program: Program): List<Diagnostic>
}
