package formatter.factory

import formatter.Formatter
import formatter.FormatterImpl
import type.Version

object DefaultFormatterFactory {
    fun createFormatter(version: Version): Formatter {
        // TODO
        return FormatterImpl()
    }
}
