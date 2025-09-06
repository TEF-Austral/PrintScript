import analyzer.AnalyzerConfig
import analyzer.AnalyzerConfigLoader
import analyzer.Diagnostic
import analyzer.IdentifierStyle
import analyzer.rules.IdentifierStyleRule
import analyzer.rules.PrintlnArgsRule
import analyzer.rules.Rule
import node.Program

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
