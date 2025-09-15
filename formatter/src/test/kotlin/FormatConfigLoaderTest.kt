import formatter.config.FormatConfig
import formatter.config.FormatConfigLoader
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import java.nio.file.Files
import java.nio.file.Path

class FormatConfigLoaderTest {
    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `load json config file`() {
        val jsonFile = tempDir.resolve("config.json")
        val jsonContent =
            """
            {
              "spaceBeforeColon": "true",
              "spaceAfterColon": "false",
              "spaceAroundAssignment": "false",
              "blankLinesAfterPrintln": "2",
              "indentSize": "6"
            }
            """.trimIndent()
        Files.writeString(jsonFile, jsonContent)

        val config = FormatConfigLoader.load(jsonFile.toString())
        assertTrue(config.spaceBeforeColon)
        assertFalse(config.spaceAfterColon)
        assertFalse(config.spaceAroundAssignment)
        assertEquals(2, config.blankLinesAfterPrintln)
        assertEquals(6, config.indentSize)
    }

    @Test
    fun `load yaml config file`() {
        val yamlFile = tempDir.resolve("settings.yml")
        val yamlContent =
            """
            # sample YAML config
            spaceBeforeColon: true
            spaceAfterColon: false
            spaceAroundAssignment: false
            blankLinesAfterPrintln: 1
            indentSize: 3
            """.trimIndent()
        Files.writeString(yamlFile, yamlContent)

        val config = FormatConfigLoader.load(yamlFile.toString())
        assertTrue(config.spaceBeforeColon)
        assertFalse(config.spaceAfterColon)
        assertFalse(config.spaceAroundAssignment)
        assertEquals(1, config.blankLinesAfterPrintln)
        assertEquals(3, config.indentSize)
    }

    @Test
    fun `defaults applied when file is empty`() {
        val jsonFile = tempDir.resolve("empty.json")
        Files.writeString(jsonFile, "{}")

        val config = FormatConfigLoader.load(jsonFile.toString())
        assertFalse(config.spaceBeforeColon)
        assertTrue(config.spaceAfterColon)
        assertTrue(config.spaceAroundAssignment)
        assertEquals(0, config.blankLinesAfterPrintln)
        assertEquals(FormatConfig.DEFAULT_INDENT_SIZE, config.indentSize)
    }
}
