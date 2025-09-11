package formatter

import formatter.config.FormatConfigLoader
import formatter.factory.FormatterFactory
import node.Program
import java.io.Writer
import type.Version

class FormatterService {
    fun formatToString(
        program: Program,
        version: Version,
        configPath: String,
    ): String {
        val config = FormatConfigLoader.load(configPath)
        val formatter = FormatterFactory.createWithVersion(version)
        return formatter.formatToString(program, config)
    }

    fun formatToWriter(
        program: Program,
        version: Version,
        configPath: String,
        writer: Writer,
    ) {
        val config = FormatConfigLoader.load(configPath)
        val formatter = FormatterFactory.createWithVersion(version)
        formatter.formatToWriter(program, config, writer)
    }
}
