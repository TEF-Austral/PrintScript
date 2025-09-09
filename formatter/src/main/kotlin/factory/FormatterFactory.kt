package formatter.factory

import formatter.DefaultFormatter
import formatter.Formatter
import formatter.rules.RuleRegistry

object FormatterFactory {
    fun create(version: String): Formatter =
        when {
            version.startsWith("1.1") -> DefaultFormatter(RuleRegistry.rulesV11)
            else -> DefaultFormatter(RuleRegistry.rulesV10)
        }
}
