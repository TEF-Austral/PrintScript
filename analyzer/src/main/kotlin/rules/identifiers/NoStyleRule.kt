package rules.identifiers

import checkers.IdentifierStyle
import config.AnalyzerConfig
import diagnostic.Diagnostic
import node.Program
import rules.Rule

class NoStyleRule : Rule {
    override fun canHandle(config: AnalyzerConfig): Boolean =
        config.identifierStyle == IdentifierStyle.NO_STYLE

    override fun apply(program: Program): List<Diagnostic> = emptyList()
}
