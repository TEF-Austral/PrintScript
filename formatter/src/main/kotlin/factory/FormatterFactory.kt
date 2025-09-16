package formatter.factory

import formatter.DefaultFormatter
import formatter.Formatter
import formatter.rules.ColonSpacingRule
import formatter.rules.FormatRule
import formatter.rules.IfBraceRule
import formatter.rules.SpaceAroundAssignmentRule
import formatter.rules.SpaceAroundOperatorsRule
import type.Version

object FormatterFactory : FormatterFactoryInterface {
    private val baseRules: List<FormatRule> = listOf(ColonSpacingRule(),
        SpaceAroundOperatorsRule(), SpaceAroundAssignmentRule())
    override fun createWithVersion(version: Version): Formatter =
        when (version) {
            Version.VERSION_1_1 -> DefaultFormatter(baseRules + listOf(IfBraceRule()))
            Version.VERSION_1_0 -> DefaultFormatter(baseRules)
        } // , EnforceSingleSpaceRule(),LineBreakRule()
    // IndentationRule(),

    override fun createCustom(rules: List<FormatRule>): Formatter = DefaultFormatter()
}
