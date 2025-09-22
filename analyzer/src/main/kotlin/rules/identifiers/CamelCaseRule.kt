package rules.identifiers

import checkers.CamelCaseChecker
import checkers.IdentifierStyle
import config.AnalyzerConfig
import diagnostic.Diagnostic
import node.Program
import rules.Rule

class CamelCaseRule : Rule {
    private val checker = CamelCaseChecker()

    override fun canHandle(config: AnalyzerConfig): Boolean =
        config.identifierStyle == IdentifierStyle.CAMEL_CASE

    override fun apply(program: Program): List<Diagnostic> = checkProgram(program, checker)
}
