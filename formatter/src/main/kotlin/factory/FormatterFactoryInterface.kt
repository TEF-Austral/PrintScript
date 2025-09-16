package formatter.factory

import formatter.Formatter
import type.Version

interface FormatterFactoryInterface {
    fun createWithVersion(version: Version): Formatter
}
