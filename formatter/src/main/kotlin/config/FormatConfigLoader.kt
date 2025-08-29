package formatter.config

import reader.JsonReader
import reader.YamlReader

object FormatConfigLoader {
    private val jsonParser = JsonFormatConfigParser()
    private val yamlParser = YamlFormatConfigParser()

    fun load(path: String): FormatConfig =
        when (path.substringAfterLast('.', "").lowercase()) {
            "yaml", "yml" -> yamlParser.parse(YamlReader(path).read())
            else          -> jsonParser.parse(JsonReader(path).read())
        }
}