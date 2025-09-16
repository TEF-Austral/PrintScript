import formatter.config.FormatConfig
import formatter.config.JsonFormatConfigParser
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertNull

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

        assertTrue(cfg.spaceBeforeColon == true)
        assertFalse(cfg.spaceAfterColon == true)
        assertFalse(cfg.spaceAroundAssignment == true)
        assertEquals(2, cfg.blankLinesAfterPrintln)
        assertEquals(8, cfg.indentSize)
    }

    @Test
    fun `defaults applied when missing`() {
        val json = "{}"
        val cfg = parser.parse(json)

        assertNull(cfg.spaceBeforeColon)
        assertNull(cfg.spaceAfterColon)
        assertNull(cfg.spaceAroundAssignment)
        assertEquals(0, cfg.blankLinesAfterPrintln)
        assertEquals(FormatConfig.DEFAULT_SIZE, cfg.indentSize)
    }
}
