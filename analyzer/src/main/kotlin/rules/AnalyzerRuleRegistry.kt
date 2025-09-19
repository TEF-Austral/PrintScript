package rules

import checkers.NameChecker

object AnalyzerRuleRegistry {
    fun rulesV10(
        styleChecker: NameChecker,
        restrictPrintlnArgs: Boolean,
        restrictReadInputArgs: Boolean,
    ): List<Rule> {
        val list = mutableListOf<Rule>()
        list += IdentifierStyleRule(styleChecker)
        if (restrictPrintlnArgs) list += PrintlnArgsRule()
        return list
    }

    fun rulesV11(
        styleChecker: NameChecker,
        restrictPrintlnArgs: Boolean,
        restrictReadInputArgs: Boolean,
    ): List<Rule> {
        val list = mutableListOf<Rule>()
        list += IdentifierStyleRule(styleChecker)
        if (restrictPrintlnArgs) list += PrintlnArgsRule()
        if (restrictReadInputArgs) list += ReadInputArgsRule()
        return list
    }
}
