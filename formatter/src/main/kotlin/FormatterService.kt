package formatter

import formatter.config.FormatConfigLoader
import formatter.factory.FormatterFactory
import node.Program
import java.io.Writer

class FormatterService {
    fun formatToString(
        program: Program,
        version: String,
        configPath: String,
    ): String {
        val config = FormatConfigLoader.load(configPath)
        val formatter = FormatterFactory.create(version)
        return formatter.formatToString(program, config)
    }

    fun formatToWriter(
        program: Program,
        version: String,
        configPath: String,
        writer: Writer,
    ) {
        val config = FormatConfigLoader.load(configPath)
        val formatter = FormatterFactory.create(version)
        formatter.formatToWriter(program, config, writer)
    }
}
