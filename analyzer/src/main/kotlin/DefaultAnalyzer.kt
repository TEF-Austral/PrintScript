import config.AnalyzerConfig
import config.AnalyzerConfigLoader
import diagnostic.Diagnostic
import node.Program
import rules.CamelCaseChecker
import rules.IdentifierStyle
import rules.IdentifierStyleRule
import rules.PrintlnArgsRule
import rules.ReadInputArgsRule
import rules.Rule
import rules.SnakeCaseChecker

class DefaultAnalyzer(
    private val config: AnalyzerConfig,
) : Analyzer {
    constructor(configPath: String?) : this(
        configPath?.let(AnalyzerConfigLoader::load) ?: AnalyzerConfig(),
    )

    private val rules: List<Rule>

    init {
        val tempRules = mutableListOf<Rule>()
        val styleChecker =
            when (config.identifierStyle) {
                IdentifierStyle.CAMEL_CASE -> CamelCaseChecker()
                IdentifierStyle.SNAKE_CASE -> SnakeCaseChecker()
            }
        tempRules.add(IdentifierStyleRule(styleChecker))
        if (config.restrictPrintlnArgs) tempRules.add(PrintlnArgsRule())
        if (config.restrictReadInputArgs) tempRules.add(ReadInputArgsRule())
        rules = tempRules
    }

    override fun analyze(program: Program): List<Diagnostic> = rules.flatMap { it.apply(program) }
}
