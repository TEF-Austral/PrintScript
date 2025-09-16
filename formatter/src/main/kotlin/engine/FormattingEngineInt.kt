package formatter.engine

import TokenStream
import formatter.config.FormatConfig

interface FormattingEngineInt {
    fun format(
        src: TokenStream,
        config: FormatConfig,
    ): String
}
