package formatter.engine

import formatter.config.FormatConfig

interface LinePostProcessor {
    fun process(
        text: String,
        config: FormatConfig,
    ): String
}
