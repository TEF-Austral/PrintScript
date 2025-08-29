import formatter.config.FormatConfig
import formatter.config.JsonFormatConfigParser
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertEquals

class JsonFormatConfigParserTest {
    private val parser = JsonFormatConfigParser()

    @Test
    fun `parse full json config`() {
        val json = """{
      "spaceBeforeColon": "true",
      "spaceAfterColon": "false",
      "spaceAroundAssignment": "false",
      "blankLinesBeforePrintln": "2",
      "indentSize": "8"
    }"""
        val cfg = parser.parse(json)
        assertTrue(cfg.spaceBeforeColon)
        assertFalse(cfg.spaceAfterColon)
        assertFalse(cfg.spaceAroundAssignment)
        assertEquals(2, cfg.blankLinesBeforePrintln)
        assertEquals(8, cfg.indentSize)
    }

    @Test
    fun `defaults applied when missing`() {
        val json = "{}"
        val cfg = parser.parse(json)
        assertFalse(cfg.spaceBeforeColon)
        assertTrue(cfg.spaceAfterColon)
        assertTrue(cfg.spaceAroundAssignment)
        assertEquals(0, cfg.blankLinesBeforePrintln)
        assertEquals(FormatConfig.DEFAULT_INDENT_SIZE, cfg.indentSize)
    }
}
