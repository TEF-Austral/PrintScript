package formatter.factory

import formatter.DefaultFormatter
import formatter.Formatter
import formatter.rules.FormatRule
import formatter.rules.RuleRegistry
import type.Version

object FormatterFactory : FormatterFactoryInterface {
    override fun createWithVersion(version: Version): Formatter =
        when (version) {
            Version.VERSION_1_1 -> DefaultFormatter(RuleRegistry.rulesV11)
            Version.VERSION_1_0 -> DefaultFormatter(RuleRegistry.rulesV10)
        }

    override fun createCustom(rules: List<FormatRule>): Formatter = DefaultFormatter(rules)
}
