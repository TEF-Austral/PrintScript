package formatter.factory

import formatter.Formatter
import formatter.rules.FormatRule
import type.Version

interface FormatterFactoryInterface {
    fun createWithVersion(version: Version): Formatter

    fun createCustom(rules: List<FormatRule>): Formatter
}
