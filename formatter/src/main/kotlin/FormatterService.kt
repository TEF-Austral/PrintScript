package formatter

import formatter.config.FormatConfig
import formatter.config.FormatConfigLoader
import java.io.Writer
import node.Program

class FormatterService(
    private val formatter: Formatter = DefaultFormatter()
) {
    fun formatToString(
        program: Program,
        configPath: String
    ): String {
        val config: FormatConfig = FormatConfigLoader.load(configPath)
        return formatter.formatToString(program, config)
    }

    fun formatToWriter(
        program: Program,
        configPath: String,
        writer: Writer
    ) {
        val config: FormatConfig = FormatConfigLoader.load(configPath)
        formatter.formatToWriter(program, config, writer)
    }
}
