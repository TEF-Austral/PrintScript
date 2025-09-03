package analyzer

import CamelCaseChecker
import SnakeCaseChecker
import analyzer.rules.IdentifierStyleRule
import analyzer.rules.PrintlnArgsRule
import analyzer.rules.Rule
import node.Program

class Analyzer(
    config: AnalyzerConfig = AnalyzerConfig(),
) {
    private val rules: List<Rule> =
        buildList {
            add(
                IdentifierStyleRule(
                    when (config.identifierStyle) {
                        IdentifierStyle.CAMEL_CASE -> CamelCaseChecker()
                        IdentifierStyle.SNAKE_CASE -> SnakeCaseChecker()
                    },
                ),
            )
            if (config.restrictPrintlnArgs) add(PrintlnArgsRule())
        }

    fun analyze(program: Program): List<Diagnostic> = rules.flatMap { it.apply(program) }
}
