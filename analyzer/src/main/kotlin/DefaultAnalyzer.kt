import config.AnalyzerConfig
import diagnostic.Diagnostic
import rules.IdentifierStyleRule
import rules.PrintlnArgsRule
import rules.Rule
import config.AnalyzerConfigLoader
import node.Program
import rules.CamelCaseChecker
import rules.IdentifierStyle
import rules.SnakeCaseChecker

class DefaultAnalyzer(
    configPath: String? = null,
) : Analyzer {
    private val config = configPath?.let(AnalyzerConfigLoader::load) ?: AnalyzerConfig()

    private val rules: List<Rule>

    init {
        val tempRules = mutableListOf<Rule>()

        // 1. Choose the identifier checker based on config
        val styleChecker =
            when (config.identifierStyle) {
                IdentifierStyle.CAMEL_CASE -> CamelCaseChecker()
                IdentifierStyle.SNAKE_CASE -> SnakeCaseChecker()
            }
        tempRules.add(IdentifierStyleRule(styleChecker))

        // 2. Optionally add the println-args rule
        if (config.restrictPrintlnArgs) {
            tempRules.add(PrintlnArgsRule())
        }

        rules = tempRules
    }

    override fun analyze(program: Program): List<Diagnostic> {
        val diagnostics = mutableListOf<Diagnostic>()
        for (rule in rules) {
            diagnostics += rule.apply(program)
        }
        return diagnostics
    }
}
