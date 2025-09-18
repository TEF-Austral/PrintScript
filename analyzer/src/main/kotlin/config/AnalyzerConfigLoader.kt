package config

import JsonReader
import YamlReader

object AnalyzerConfigLoader {
    private val jsonParser = JsonAnalyzerConfigParser()
    private val yamlParser = YamlAnalyzerConfigParser()

    fun loadAnalyzerConfig(path: String): AnalyzerConfig =
        when (path.substringAfterLast('.', "").lowercase()) {
            "yaml", "yml" -> yamlParser.parse(YamlReader(path).read())
            else -> jsonParser.parse(JsonReader(path).read())
        }
}
