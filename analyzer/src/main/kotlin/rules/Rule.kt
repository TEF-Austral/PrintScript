package analyzer.rules

import node.Program
import analyzer.Diagnostic

interface Rule {
    fun apply(program: Program): List<Diagnostic>
}
