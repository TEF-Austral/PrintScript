package factory

import Analyzer
import DefaultAnalyzer
import config.AnalyzerConfig
import config.AnalyzerConfigLoader

object AnalyzerFactory {
    fun create(
        version: String,
        configPath: String? = null,
    ): Analyzer {
        val baseConfig = configPath?.let(AnalyzerConfigLoader::load) ?: AnalyzerConfig()
        val parts = version.split('.').mapNotNull { it.toIntOrNull() }
        val major = parts.getOrNull(0) ?: 0
        val minor = parts.getOrNull(1) ?: 0

        val finalConfig =
            if (major > 1 || (major == 1 && minor >= 1)) {
                // 1.1 and above: always enable readInput, retain println flag
                baseConfig.copy(restrictReadInputArgs = true)
            } else {
                // 1.0.x: always disable readInput, respect println flag from config
                baseConfig.copy(restrictReadInputArgs = false)
            }

        return DefaultAnalyzer(finalConfig)
    }
}
