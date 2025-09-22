package rules

import diagnostic.Diagnostic
import node.Program

interface RuleEnforcer {
    fun apply(program: Program): List<Diagnostic>
}
