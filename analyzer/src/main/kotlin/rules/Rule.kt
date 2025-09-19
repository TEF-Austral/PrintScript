package rules

import node.Program
import diagnostic.Diagnostic

interface Rule {
    fun canHandle(summary: ProgramSummary): Boolean = true

    fun canHandle(program: Program): Boolean = true

    fun apply(program: Program): List<Diagnostic>
}
