package rules.identifiers

import checkers.IdentifierStyle
import checkers.SnakeCaseChecker
import config.AnalyzerConfig
import diagnostic.Diagnostic
import node.Program
import rules.Rule

class SnakeCaseRule : Rule {
    private val checker = SnakeCaseChecker()

    override fun canHandle(config: AnalyzerConfig): Boolean =
        config.identifierStyle == IdentifierStyle.SNAKE_CASE

    override fun apply(program: Program): List<Diagnostic> = checkProgram(program, checker)
}
