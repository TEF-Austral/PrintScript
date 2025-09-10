package rules

object AnalyzerRuleRegistry {
    fun rulesV10(
        styleChecker: NameChecker,
        restrictPrintlnArgs: Boolean,
    ): List<Rule> {
        val list = mutableListOf<Rule>()
        list += IdentifierStyleRule(styleChecker)
        if (restrictPrintlnArgs) list += PrintlnArgsRule()
        return list
    }

    fun rulesV11(
        styleChecker: NameChecker,
        restrictPrintlnArgs: Boolean,
    ): List<Rule> {
        val list = mutableListOf<Rule>()
        list += IdentifierStyleRule(styleChecker)
        if (restrictPrintlnArgs) list += PrintlnArgsRule()
        list += ReadInputArgsRule()
        return list
    }
}
