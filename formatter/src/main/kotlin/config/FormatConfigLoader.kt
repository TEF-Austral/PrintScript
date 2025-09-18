package formatter.config

import JsonReader
import YamlReader

object FormatConfigLoader {
    private val jsonParser = JsonFormatConfigParser()
    private val yamlParser = YamlFormatConfigParser()

    fun loadFormatConfig(path: String): FormatConfig =
        when (path.substringAfterLast('.', "").lowercase()) {
            "yaml", "yml" -> yamlParser.parse(YamlReader(path).read())
            else -> jsonParser.parse(JsonReader(path).read())
        }
}
