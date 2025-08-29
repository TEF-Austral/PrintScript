import formatter.config.FormatConfig
import formatter.config.YamlFormatConfigParser
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class YamlFormatConfigParserTest {
    private val parser = YamlFormatConfigParser()

    @Test
    fun `parse full yaml config`() {
        val yaml = """
      spaceBeforeColon: true
      spaceAfterColon: false
      spaceAroundAssignment: false
      blankLinesBeforePrintln: 2
      indentSize: 6
    """
        val cfg = parser.parse(yaml)
        assertTrue(cfg.spaceBeforeColon)
        assertFalse(cfg.spaceAfterColon)
        assertFalse(cfg.spaceAroundAssignment)
        assertEquals(2, cfg.blankLinesBeforePrintln)
        assertEquals(6, cfg.indentSize)
    }

    @Test
    fun `defaults applied when missing`() {
        val yaml = ""
        val cfg = parser.parse(yaml)
        assertFalse(cfg.spaceBeforeColon)
        assertTrue(cfg.spaceAfterColon)
        assertTrue(cfg.spaceAroundAssignment)
        assertEquals(0, cfg.blankLinesBeforePrintln)
        assertEquals(FormatConfig.DEFAULT_INDENT_SIZE, cfg.indentSize)
    }
}