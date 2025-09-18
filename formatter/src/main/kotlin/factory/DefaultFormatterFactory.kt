package formatter.factory

import formatter.Formatter
import formatter.DefaultFormatter
import type.Version

object DefaultFormatterFactory {
    fun createFormatter(version: Version): Formatter {
        // TODO
        return DefaultFormatter()
    }
}
