package formatter

import TokenStream
import formatter.config.FormatConfig
import java.io.Writer

interface Formatter {

    fun formatToString(
        src: TokenStream,
        config: FormatConfig,
    ): String

    fun formatToWriter(
        src: TokenStream,
        config: FormatConfig,
        writer: Writer,
    )
}
