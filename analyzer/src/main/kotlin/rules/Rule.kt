package rules

import node.Program
import diagnostic.Diagnostic

interface Rule {
    fun apply(program: Program): List<Diagnostic>
}
