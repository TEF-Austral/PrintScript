import formatter.config.FormatConfig
import formatter.config.JsonFormatConfigParser
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

class JsonFormatConfigParserTest {
    private val parser = JsonFormatConfigParser()

    @Test
    fun `parse full json config`() {
        val json = """{
          "spaceBeforeColon": "true",
          "spaceAfterColon": "false",
          "spaceAroundAssignment": "false",
          "blankLinesAfterPrintln": "2",
          "indentSize": "8"
        }"""
        val cfg = parser.parse(json)

        assertTrue(cfg.spaceBeforeColon)
        assertFalse(cfg.spaceAfterColon)
        assertFalse(cfg.spaceAroundAssignment)
        assertEquals(2, cfg.blankLinesAfterPrintln)
        assertEquals(8, cfg.indentSize)
    }

    @Test
    fun `defaults applied when missing`() {
        val json = "{}"
        val cfg = parser.parse(json)

        assertFalse(cfg.spaceBeforeColon)
        assertFalse(cfg.spaceAfterColon)
        assertTrue(cfg.spaceAroundAssignment)
        assertEquals(0, cfg.blankLinesAfterPrintln)
        assertEquals(FormatConfig.DEFAULT_INDENT_SIZE, cfg.indentSize)
    }
}
