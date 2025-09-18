// Archivo: formatter/FormatterImpl.kt
package formatter

import TokenStream
import formatter.config.FormatConfig
import formatter.factory.DefaultRules
import formatter.engine.DefaultLinePostProcessor
import formatter.engine.FormattingEngine
import java.io.Writer

class FormatterImpl : Formatter {

    private fun engine(): FormattingEngine =
        FormattingEngine(
            rootRule = DefaultRules.createDefaultRegistryRule(),
            postProcessors = listOf(DefaultLinePostProcessor()),
        )

    override fun formatToString(
        src: TokenStream,
        config: FormatConfig,
    ): String = engine().format(src, config)

    override fun formatToWriter(
        src: TokenStream,
        config: FormatConfig,
        writer: Writer,
    ) {
        val formatted = formatToString(src, config)
        writer.write(formatted)
        writer.flush()
    }
}
