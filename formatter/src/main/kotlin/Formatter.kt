// src/main/kotlin/formatter/Formatter.kt
package formatter

import formatter.config.FormatConfig
import node.Program
import java.io.Writer

interface Formatter {
    fun formatToString(
        program: Program,
        config: FormatConfig
    ): String

    fun formatToWriter(
        program: Program,
        config: FormatConfig,
        writer: Writer
    )
}
