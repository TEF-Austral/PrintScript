package rules

import node.Program
import scanner.StatementScanner

data class ProgramSummary(
    val hasIdentifier: Boolean,
    val hasPrintStatement: Boolean,
    val hasReadInput: Boolean,
) {
    companion object {
        fun from(program: Program): ProgramSummary = StatementScanner().scan(program)
    }
}
